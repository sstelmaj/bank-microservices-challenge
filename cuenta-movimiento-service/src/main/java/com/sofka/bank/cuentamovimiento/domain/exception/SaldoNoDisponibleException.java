package com.sofka.bank.cuentamovimiento.domain.exception;

public class SaldoNoDisponibleException extends RuntimeException {

    public SaldoNoDisponibleException(String message) {
        super(message);
    }
}
