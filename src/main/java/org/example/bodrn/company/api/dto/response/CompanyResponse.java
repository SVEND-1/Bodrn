package org.example.bodrn.company.api.dto.response;

import java.time.LocalDateTime;

public record CompanyResponse(
        Long id,
        String name,
        String description,
        String address,
        String phone,
        String workHoursWeekdays,
        String workHoursWeekends,
        LocalDateTime updatedAt
) {}