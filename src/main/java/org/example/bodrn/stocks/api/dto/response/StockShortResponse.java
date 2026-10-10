package org.example.bodrn.stocks.api.dto.response;

import java.time.LocalDateTime;

public record StockShortResponse(
        Long id,
        String title,
        String photo,
        LocalDateTime validFrom,
        LocalDateTime validTo
) {}