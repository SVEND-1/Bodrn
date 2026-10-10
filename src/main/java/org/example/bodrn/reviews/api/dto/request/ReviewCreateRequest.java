package org.example.bodrn.reviews.api.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewCreateRequest(

        @NotBlank
        @Size(max = 255)
        String authorName,

        @NotNull
        @Min(1)
        @Max(5)
        Integer rating,

        @Size(max = 3000)
        String text
) {}