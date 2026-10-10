package org.example.bodrn.stocks.api.dto.request;

import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record StockUpdateRequest(

        @Size(max = 255)
        @BindParam("title")
        String title,

        @Size(max = 3000)
        @BindParam("description")
        String description,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @BindParam("validFrom")
        LocalDateTime validFrom,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @BindParam("validTo")
        LocalDateTime validTo,

        /** Новое фото (если передано — заменяет старое). */
        @BindParam("photo")
        MultipartFile photo,

        /** true — удалить текущее фото без замены. */
        @BindParam("removePhoto")
        Boolean removePhoto
) {}