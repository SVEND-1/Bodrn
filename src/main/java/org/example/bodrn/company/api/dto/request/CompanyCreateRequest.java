package org.example.bodrn.company.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CompanyCreateRequest(

        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 3000)
        String description,

        @Size(max = 512)
        String address,

        @Size(max = 64)
        String phone,

        @Size(max = 128)
        String workHoursWeekdays,

        @Size(max = 128)
        String workHoursWeekends
) {}