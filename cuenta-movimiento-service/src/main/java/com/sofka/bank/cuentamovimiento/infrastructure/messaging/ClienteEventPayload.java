package com.sofka.bank.cuentamovimiento.infrastructure.messaging;

public interface ClienteEventPayload {

    Long clienteId();

    String nombre();

    String identificacion();

    boolean estado();
}
