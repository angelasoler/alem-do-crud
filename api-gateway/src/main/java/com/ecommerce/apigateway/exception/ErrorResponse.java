package com.ecommerce.apigateway.exception;

import java.time.Instant;

public record ErrorResponse(String error, String message, Instant timestamp) {
}
