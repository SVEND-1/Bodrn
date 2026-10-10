package org.example.bodrn.comm;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ErrorDto {
    private String error;
    private String message;
}