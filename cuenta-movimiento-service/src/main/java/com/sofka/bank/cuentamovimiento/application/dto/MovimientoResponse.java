package com.sofka.bank.cuentamovimiento.application.dto;

import com.sofka.bank.cuentamovimiento.domain.model.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoResponse(
        Long id,
        LocalDateTime fecha,
        TipoMovimiento tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldo,
        String numeroCuenta
) {
}
