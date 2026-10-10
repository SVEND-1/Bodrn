package org.example.bodrn.admin.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AdminUserCreateRequest(

        @NotBlank
        @Size(min = 3, max = 64)
        String userName,

        @NotBlank
        @Size(min = 8, max = 100)
        String password,

        Boolean isActive
) {}