package com.sofka.bank.cuentamovimiento.application.dto;

import com.sofka.bank.cuentamovimiento.domain.model.TipoCuenta;

import java.math.BigDecimal;

public record CuentaResponse(
        Long id,
        String numeroCuenta,
        TipoCuenta tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        boolean estado,
        Long clienteId
) {
}
