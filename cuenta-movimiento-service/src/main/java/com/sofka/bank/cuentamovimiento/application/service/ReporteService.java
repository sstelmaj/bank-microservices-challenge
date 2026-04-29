package com.sofka.bank.cuentamovimiento.application.service;

import com.sofka.bank.cuentamovimiento.application.dto.ReporteCuentaResponse;
import com.sofka.bank.cuentamovimiento.application.dto.ReporteEstadoCuentaResponse;
import com.sofka.bank.cuentamovimiento.application.dto.ReporteMovimientoResponse;
import com.sofka.bank.cuentamovimiento.domain.model.Cuenta;
import com.sofka.bank.cuentamovimiento.domain.model.Movimiento;
import com.sofka.bank.cuentamovimiento.domain.repository.CuentaRepository;
import com.sofka.bank.cuentamovimiento.domain.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReporteService {

    private static final String RANGO_FECHAS_INVALIDO_MESSAGE = "Rango de fechas invalido";

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public ReporteService(CuentaRepository cuentaRepository, MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public ReporteEstadoCuentaResponse generarEstadoCuenta(Long clienteId, String fecha) {
        RangoFechas rangoFechas = parsearRango(fecha);
        List<ReporteCuentaResponse> cuentas = cuentaRepository.findAllByClienteIdOrderByIdAsc(clienteId).stream()
                .map(cuenta -> toCuentaResponse(cuenta, rangoFechas))
                .toList();

        return new ReporteEstadoCuentaResponse(
                clienteId,
                rangoFechas.fechaInicio(),
                rangoFechas.fechaFin(),
                cuentas
        );
    }

    private ReporteCuentaResponse toCuentaResponse(Cuenta cuenta, RangoFechas rangoFechas) {
        List<ReporteMovimientoResponse> movimientos = movimientoRepository
                .findAllByCuentaIdAndFechaBetweenOrderByFechaAscIdAsc(
                        cuenta.getId(),
                        rangoFechas.fechaHoraInicio(),
                        rangoFechas.fechaHoraFin()
                )
                .stream()
                .map(this::toMovimientoResponse)
                .toList();

        return new ReporteCuentaResponse(
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta().name(),
                cuenta.getSaldoInicial(),
                cuenta.isEstado(),
                cuenta.getSaldoDisponible(),
                movimientos
        );
    }

    private ReporteMovimientoResponse toMovimientoResponse(Movimiento movimiento) {
        return new ReporteMovimientoResponse(
                movimiento.getFecha().toLocalDate(),
                movimiento.getValor(),
                movimiento.getSaldo()
        );
    }

    private RangoFechas parsearRango(String fecha) {
        String[] partes = fecha.split(",");
        if (partes.length != 2) {
            throw new IllegalArgumentException(RANGO_FECHAS_INVALIDO_MESSAGE);
        }

        try {
            LocalDate fechaInicio = LocalDate.parse(partes[0].trim());
            LocalDate fechaFin = LocalDate.parse(partes[1].trim());

            if (fechaInicio.isAfter(fechaFin)) {
                throw new IllegalArgumentException(RANGO_FECHAS_INVALIDO_MESSAGE);
            }

            return new RangoFechas(
                    fechaInicio,
                    fechaFin,
                    fechaInicio.atStartOfDay(),
                    fechaFin.atTime(LocalTime.MAX)
            );
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException(RANGO_FECHAS_INVALIDO_MESSAGE);
        }
    }

    private record RangoFechas(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            LocalDateTime fechaHoraInicio,
            LocalDateTime fechaHoraFin
    ) {
    }
}
