package org.example.bodrn.product.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProductCreateRequest(

        @NotBlank
        @Size(max = 255)
        @BindParam("title")
        String title,

        @Size(max = 3000)
        @BindParam("description")
        String description,

        @NotBlank
        @BindParam("price")
        String price,

        @NotNull
        @BindParam("categoryId")
        Long categoryId,

        @BindParam("photos")
        List<MultipartFile> photos
) {}