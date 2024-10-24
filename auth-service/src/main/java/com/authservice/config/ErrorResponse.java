package com.authservice.config;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class ErrorResponse {
    private String status;
    private String message;
    private boolean success;
}