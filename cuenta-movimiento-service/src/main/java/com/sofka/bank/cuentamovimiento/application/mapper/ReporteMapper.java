package com.sofka.bank.cuentamovimiento.application.mapper;

import com.sofka.bank.cuentamovimiento.application.dto.ReporteCuentaResponse;
import com.sofka.bank.cuentamovimiento.application.dto.ReporteEstadoCuentaResponse;
import com.sofka.bank.cuentamovimiento.application.dto.ReporteMovimientoResponse;
import com.sofka.bank.cuentamovimiento.domain.model.Cuenta;
import com.sofka.bank.cuentamovimiento.domain.model.Movimiento;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ReporteMapper {

    public ReporteEstadoCuentaResponse toEstadoCuentaResponse(
            Long clienteId,
            LocalDate fechaInicio,
            LocalDate fechaFin,
            List<ReporteCuentaResponse> cuentas
    ) {
        return new ReporteEstadoCuentaResponse(clienteId, fechaInicio, fechaFin, cuentas);
    }

    public ReporteCuentaResponse toCuentaResponse(Cuenta cuenta, List<ReporteMovimientoResponse> movimientos) {
        return new ReporteCuentaResponse(
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta().name(),
                cuenta.getSaldoInicial(),
                cuenta.isEstado(),
                cuenta.getSaldoDisponible(),
                movimientos
        );
    }

    public ReporteMovimientoResponse toMovimientoResponse(Movimiento movimiento) {
        return new ReporteMovimientoResponse(
                movimiento.getFecha().toLocalDate(),
                movimiento.getValor(),
                movimiento.getSaldo()
        );
    }
}
