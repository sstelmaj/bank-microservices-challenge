package com.sofka.bank.cuentamovimiento.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record ReporteCuentaResponse(
        String numeroCuenta,
        String tipo,
        BigDecimal saldoInicial,
        boolean estado,
        BigDecimal saldoDisponible,
        List<ReporteMovimientoResponse> movimientos
) {
}
