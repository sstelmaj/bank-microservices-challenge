package com.sofka.bank.cuentamovimiento.domain.exception;

public class ClienteSnapshotNotFoundException extends RuntimeException {

    public ClienteSnapshotNotFoundException(String message) {
        super(message);
    }
}
