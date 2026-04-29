package com.sofka.bank.cuentamovimiento.domain.exception;

public class DuplicatedNumeroCuentaException extends RuntimeException {

    public DuplicatedNumeroCuentaException(String message) {
        super(message);
    }
}
