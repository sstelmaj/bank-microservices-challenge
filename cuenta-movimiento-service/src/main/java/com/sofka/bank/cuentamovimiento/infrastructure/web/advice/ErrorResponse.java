package com.sofka.bank.cuentamovimiento.infrastructure.web.advice;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
