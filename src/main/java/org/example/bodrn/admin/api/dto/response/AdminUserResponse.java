package org.example.bodrn.admin.api.dto.response;

import java.time.LocalDateTime;

public record AdminUserResponse(
        Long id,
        String userName,
        Boolean isActive,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}