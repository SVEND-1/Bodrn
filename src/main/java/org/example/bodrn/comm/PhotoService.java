package org.example.bodrn.comm;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.UUID;

@Slf4j
@Service
public class PhotoService {

    private static final int MAX_SIDE = 1920;
    private static final float JPEG_QUALITY = 0.85f;

    @Value("${app.upload.root:uploads}")
    private String uploadRoot;

    @Value("${app.upload.url-prefix:/uploads/files}")
    private String urlPrefix;

    public byte[] compress(MultipartFile file) {
        try (InputStream in = file.getInputStream()) {
            return compressInternal(in, file.getContentType());
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось обработать изображение: " + file.getOriginalFilename(), e);
        }
    }

    private byte[] compressInternal(InputStream in, String contentType) throws IOException {
        BufferedImage source = ImageIO.read(in);
        if (source == null) {
            throw new IllegalArgumentException("Файл не является изображением");
        }

        BufferedImage resized = resizeIfNeeded(source);

        String format = (contentType != null && contentType.contains("png")) ? "png" : "jpg";
        if ("png".equals(format)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, "png", baos);
            return baos.toByteArray();
        }
        return writeJpeg(resized, JPEG_QUALITY);
    }

    private BufferedImage resizeIfNeeded(BufferedImage source) throws IOException {
        int w = source.getWidth();
        int h = source.getHeight();
        if (w <= MAX_SIDE && h <= MAX_SIDE) {
            return source;
        }
        return Thumbnails.of(source)
                .size(MAX_SIDE, MAX_SIDE)
                .keepAspectRatio(true)
                .asBufferedImage();
    }

    private byte[] writeJpeg(BufferedImage image, float quality) throws IOException {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpeg");
        if (!writers.hasNext()) {
            throw new IllegalStateException("JPEG writer не найден");
        }
        ImageWriter writer = writers.next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
            writer.dispose();
            return baos.toByteArray();
        }
    }

    public String save(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Файл пустой");
        }

        byte[] data = compress(file);
        String contentType = file.getContentType();
        String ext = (contentType != null && contentType.contains("png")) ? ".png" : ".jpg";

        String originalName = file.getOriginalFilename();
        String baseName = sanitizeBaseName(originalName);
        String fileName = baseName + "_" + UUID.randomUUID() + ext;

        try {
            Path dir = Paths.get(uploadRoot, "files").toAbsolutePath().normalize();
            Files.createDirectories(dir);

            Path target = dir.resolve(fileName).normalize();
            if (!target.startsWith(dir)) {
                throw new IllegalArgumentException("Некорректный путь сохранения");
            }

            Files.write(target, data);

            String url = urlPrefix + "/" + fileName;
            return url;
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось сохранить файл: " + originalName, e);
        }
    }

    public void delete(String url) {
        if (url == null || url.isBlank()) {
            return;
        }

        String relative = url;
        int idx = url.indexOf(urlPrefix);
        if (idx >= 0) {
            relative = url.substring(idx + urlPrefix.length());
        } else {
            int slash = url.lastIndexOf('/');
            relative = (slash >= 0) ? url.substring(slash + 1) : url;
        }

        int q = relative.indexOf('?');
        if (q >= 0) {
            relative = relative.substring(0, q);
        }

        while (relative.startsWith("/")) {
            relative = relative.substring(1);
        }

        if (relative.isBlank()) {
            return;
        }

        Path dir = Paths.get(uploadRoot, "files").toAbsolutePath().normalize();
        Path target = dir.resolve(relative).normalize();
        if (!target.startsWith(dir)) {
            log.warn("Попытка удалить файл вне разрешённой директории: {}", url);
            return;
        }

        try {
            boolean deleted = Files.deleteIfExists(target);
            if (deleted) {
                log.info("Файл удалён: {}", target);
            } else {
                log.warn("Файл для удаления не найден: {}", target);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось удалить файл: " + url, e);
        }
    }

    private String sanitizeBaseName(String originalName) {
        if (originalName == null || originalName.isBlank()) {
            return "file";
        }
        String name = originalName;
        int slash = Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\'));
        if (slash >= 0) {
            name = name.substring(slash + 1);
        }
        int dot = name.lastIndexOf('.');
        if (dot > 0) {
            name = name.substring(0, dot);
        }
        name = name.replaceAll("[^a-zA-Z0-9._-]", "_");
        if (name.isBlank()) {
            name = "file";
        }
        if (name.length() > 50) {
            name = name.substring(0, 50);
        }
        return name;
    }
}