package org.example.bodrn.stocks.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StockShortResponse(
        Long id,
        String title,
        String description,
        String photo,
        BigDecimal priceBefore,
        BigDecimal priceAfter,
        LocalDateTime validFrom,
        LocalDateTime validTo
) {}