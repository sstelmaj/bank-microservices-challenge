package com.sofka.bank.cuentamovimiento.domain.exception;

public class CuentaNotFoundException extends RuntimeException {

    public CuentaNotFoundException(String message) {
        super(message);
    }
}
