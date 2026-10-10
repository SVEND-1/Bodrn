package org.example.bodrn.security;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Converter(autoApply = false)
@Component
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private final CryptoService crypto;

    public EncryptedStringConverter(CryptoService crypto) {
        this.crypto = crypto;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        return crypto.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return crypto.decrypt(dbData);
        } catch (Exception e) {
            log.error("Failed to decrypt column value", e);
            throw e;
        }
    }
}