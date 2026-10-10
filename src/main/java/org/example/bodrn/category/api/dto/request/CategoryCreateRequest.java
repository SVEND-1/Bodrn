package org.example.bodrn.category.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.BindParam;

public record CategoryCreateRequest(

        @NotBlank
        @Size(max = 128)
        @BindParam("name")
        String name
) {}