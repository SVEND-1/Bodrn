package org.example.bodrn.admin.api.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdminDto {
    private Long id;
    private String username;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
