package org.example.bodrn.security;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

@Slf4j
@Service
public class CryptoService {

    private static final int GCM_IV_LEN = 12;
    private static final int GCM_TAG_LEN = 128;

    private final SecretKeySpec aesKey;
    private final SecretKeySpec hmacKey;
    private final SecureRandom random = new SecureRandom();

    public CryptoService(
            @Value("${app.crypto.aes-key}") String aesKeyB64,
            @Value("${app.crypto.hmac-key}") String hmacKeyB64) {

        byte[] aes = decodeKey(aesKeyB64, "app.crypto.aes-key");
        byte[] hmac = decodeKey(hmacKeyB64, "app.crypto.hmac-key");

        if (aes.length != 16 && aes.length != 24 && aes.length != 32) {
            throw new IllegalStateException(
                    "app.crypto.aes-key must be 16/24/32 bytes, got " + aes.length);
        }
        if (hmac.length < 32) {
            throw new IllegalStateException(
                    "app.crypto.hmac-key must be >= 32 bytes, got " + hmac.length);
        }
        if (MessageDigest.isEqual(aes, hmac)) {
            throw new IllegalStateException("aes-key and hmac-key must be different");
        }

        this.aesKey = new SecretKeySpec(aes, "AES");
        this.hmacKey = new SecretKeySpec(hmac, "HmacSHA256");
    }

    @PostConstruct
    void wipe() {
        // ключи уже в SecretKeySpec; в byte[] держать не нужно
    }

    /** AES-GCM, возвращает Base64(IV || ciphertext||tag). */
    public String encrypt(String plain) {
        if (plain == null) return null;
        try {
            byte[] iv = new byte[GCM_IV_LEN];
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LEN, iv));
            byte[] ct = cipher.doFinal(plain.getBytes(StandardCharsets.UTF_8));

            byte[] out = new byte[iv.length + ct.length];
            System.arraycopy(iv, 0, out, 0, iv.length);
            System.arraycopy(ct, 0, out, iv.length, ct.length);
            return Base64.getEncoder().encodeToString(out);
        } catch (Exception e) {
            throw new IllegalStateException("encrypt failed", e);
        }
    }

    public String decrypt(String encoded) {
        if (encoded == null) return null;
        try {
            byte[] in = Base64.getDecoder().decode(encoded);
            if (in.length < GCM_IV_LEN + GCM_TAG_LEN / 8) {
                throw new IllegalStateException("ciphertext too short");
            }
            byte[] iv = new byte[GCM_IV_LEN];
            System.arraycopy(in, 0, iv, 0, GCM_IV_LEN);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(GCM_TAG_LEN, iv));
            byte[] pt = cipher.doFinal(in, GCM_IV_LEN, in.length - GCM_IV_LEN);
            return new String(pt, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("decrypt failed", e);
        }
    }

    /** Детерминированный HMAC-SHA256, hex. Для blind-index (поиск/unique). */
    public String blindIndex(String value) {
        if (value == null) return null;
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(hmacKey);
            byte[] h = mac.doFinal(normalize(value).getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(h.length * 2);
            for (byte b : h) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("hmac failed", e);
        }
    }

    /**
     * Нормализация для blind-index.
     * Сейчас: trim + lowercase.
     * Если blind-index для телефонов — раскомментируй digits-only.
     */
    private String normalize(String v) {
        String s = v.trim().toLowerCase();
        // для телефонов:
        // s = s.replaceAll("[^0-9]", "");
        // if (s.startsWith("8")) s = "7" + s.substring(1);
        // if (s.startsWith("+")) s = s.substring(1);
        return s;
    }

    private static byte[] decodeKey(String b64, String name) {
        if (b64 == null || b64.isBlank()) {
            throw new IllegalStateException(name + " is not configured");
        }
        try {
            return Base64.getDecoder().decode(b64);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(name + " is not valid Base64", e);
        }
    }
}