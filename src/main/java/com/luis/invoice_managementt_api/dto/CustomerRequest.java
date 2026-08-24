package com.luis.invoice_managementt_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerRequest(

    @NotBlank(message = "Name is required")
    @Size( max = 50, message = "Name must be between 2 and 50 characters")
    String name,


    @NotBlank(message = "Email is required")
    @Size(max = 100, message = "Email must be between 2 and 100 characters")
    String email,

    @NotBlank(message = "Document is required")
    @Size(max = 20, message = "Document must have at most 20 characters")
    String document
) {
}