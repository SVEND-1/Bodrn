package org.example.bodrn.product.api.dto.response;

public record ProductShortResponse(
        Long id,
        String title,
        String price,
        String mainPhoto
) {}