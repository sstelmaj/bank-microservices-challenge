package com.sofka.bank.cuentamovimiento.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReporteMovimientoResponse(
        LocalDate fecha,
        BigDecimal movimiento,
        BigDecimal saldoDisponible
) {
}
