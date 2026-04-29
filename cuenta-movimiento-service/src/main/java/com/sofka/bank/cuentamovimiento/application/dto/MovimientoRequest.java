package com.sofka.bank.cuentamovimiento.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MovimientoRequest(
        @NotBlank(message = "numeroCuenta es obligatorio") String numeroCuenta,
        @NotNull(message = "valor es obligatorio") BigDecimal valor
) {
}
