package com.sofka.bank.cuentamovimiento.domain.exception;

public class MovimientoAplicadoNoModificableException extends RuntimeException {

    public MovimientoAplicadoNoModificableException(String message) {
        super(message);
    }
}
