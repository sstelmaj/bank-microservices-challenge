package com.sofka.bank.cuentamovimiento.application.dto;

import com.sofka.bank.cuentamovimiento.domain.model.TipoCuenta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CuentaRequest(
        @NotBlank(message = "numeroCuenta es obligatorio") String numeroCuenta,
        @NotNull(message = "tipoCuenta es obligatorio") TipoCuenta tipoCuenta,
        @NotNull(message = "saldoInicial es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial no puede ser negativo")
        BigDecimal saldoInicial,
        @NotNull(message = "estado es obligatorio") Boolean estado,
        @NotNull(message = "clienteId es obligatorio") Long clienteId
) {
}
