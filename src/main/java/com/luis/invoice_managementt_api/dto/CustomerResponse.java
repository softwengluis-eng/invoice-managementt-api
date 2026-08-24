package com.luis.invoice_managementt_api.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
    Long id,
    String name,
    String email,
    String document,
    LocalDateTime createdAt,
    LocalDateTime updatedAt 
) {
}
