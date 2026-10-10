package org.example.bodrn.product.api.dto.request;

import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.BindParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record ProductUpdateRequest(

        @Size(max = 255)
        @BindParam("title")
        String title,

        @Size(max = 3000)
        @BindParam("description")
        String description,

        @BindParam("price")
        String price,

        @BindParam("categoryId")
        Long categoryId,

        @BindParam("photos")
        List<MultipartFile> photos,

        @BindParam("keepPhotos")
        List<String> keepPhotos
) {}