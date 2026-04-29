package com.sofka.bank.cuentamovimiento.domain.exception;

public class ClienteInactivoException extends RuntimeException {

    public ClienteInactivoException(String message) {
        super(message);
    }
}
