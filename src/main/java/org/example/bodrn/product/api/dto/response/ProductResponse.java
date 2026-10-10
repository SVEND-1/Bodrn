package org.example.bodrn.product.api.dto.response;

import java.util.List;

public record ProductResponse(
        Long id,
        String title,
        String description,
        String price,
        Long categoryId,
        String categoryName,
        List<String> photos
) {}