package com.sofka.bank.cuentamovimiento.domain.exception;

public class MovimientoNotFoundException extends RuntimeException {

    public MovimientoNotFoundException(String message) {
        super(message);
    }
}
