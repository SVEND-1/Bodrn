package org.example.bodrn.stocks.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockCreateRequest(

        @NotBlank
        @Size(max = 255)
        @BindParam("title")
        String title,

        @Size(max = 3000)
        @BindParam("description")
        String description,

        @BindParam("priceBefore")
        BigDecimal priceBefore,

        @BindParam("priceAfter")
        BigDecimal priceAfter,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @BindParam("validFrom")
        LocalDateTime validFrom,

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        @BindParam("validTo")
        LocalDateTime validTo,

        @BindParam("photo")
        MultipartFile photo
) {}