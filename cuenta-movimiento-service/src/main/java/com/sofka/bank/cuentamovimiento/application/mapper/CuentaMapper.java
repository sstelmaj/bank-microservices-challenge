package com.sofka.bank.cuentamovimiento.application.mapper;

import com.sofka.bank.cuentamovimiento.application.dto.CuentaRequest;
import com.sofka.bank.cuentamovimiento.application.dto.CuentaResponse;
import com.sofka.bank.cuentamovimiento.domain.model.Cuenta;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    public Cuenta toEntity(CuentaRequest request) {
        return new Cuenta(
                null,
                request.numeroCuenta(),
                request.tipoCuenta(),
                request.saldoInicial(),
                request.estado(),
                request.clienteId()
        );
    }

    public void updateEntity(Cuenta cuenta, CuentaRequest request) {
        cuenta.actualizar(
                request.numeroCuenta(),
                request.tipoCuenta(),
                request.saldoInicial(),
                request.estado(),
                request.clienteId()
        );
    }

    public CuentaResponse toResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.isEstado(),
                cuenta.getClienteId()
        );
    }
}
