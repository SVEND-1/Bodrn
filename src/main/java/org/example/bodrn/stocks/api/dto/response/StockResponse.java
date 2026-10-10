package org.example.bodrn.stocks.api.dto.response;

import java.time.LocalDateTime;

public record StockResponse(
        Long id,
        String title,
        String description,
        String photo,
        LocalDateTime validFrom,
        LocalDateTime validTo
) {}