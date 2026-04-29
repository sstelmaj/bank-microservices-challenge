package com.sofka.bank.cuentamovimiento.application.mapper;

import com.sofka.bank.cuentamovimiento.application.dto.MovimientoRequest;
import com.sofka.bank.cuentamovimiento.application.dto.MovimientoResponse;
import com.sofka.bank.cuentamovimiento.domain.model.Cuenta;
import com.sofka.bank.cuentamovimiento.domain.model.Movimiento;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class MovimientoMapper {

    public Movimiento toEntity(
            MovimientoRequest request,
            Cuenta cuenta,
            BigDecimal saldoResultante,
            LocalDateTime fecha
    ) {
        return new Movimiento(
                null,
                fecha,
                request.valor(),
                saldoResultante,
                cuenta
        );
    }

    public MovimientoResponse toResponse(Movimiento movimiento) {
        return new MovimientoResponse(
                movimiento.getId(),
                movimiento.getFecha(),
                movimiento.getTipoMovimiento(),
                movimiento.getValor(),
                movimiento.getSaldo(),
                movimiento.getCuenta().getNumeroCuenta()
        );
    }
}
