package org.example.bodrn.admin.api.dto.request;

import jakarta.validation.constraints.Size;

public record AdminUserUpdateRequest(

        @Size(min = 3, max = 64)
        String userName,

        /** null — не менять пароль. */
        @Size(min = 8, max = 100)
        String password,

        Boolean isActive
) {}