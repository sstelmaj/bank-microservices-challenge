package com.sofka.bank.cuentamovimiento.application.dto;

import java.time.LocalDate;
import java.util.List;

public record ReporteEstadoCuentaResponse(
        Long clienteId,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<ReporteCuentaResponse> cuentas
) {
}
