package org.example.bodrn.reviews.api.dto.response;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        String authorName,
        Integer rating,
        String text,
        LocalDateTime createdAt
) {}