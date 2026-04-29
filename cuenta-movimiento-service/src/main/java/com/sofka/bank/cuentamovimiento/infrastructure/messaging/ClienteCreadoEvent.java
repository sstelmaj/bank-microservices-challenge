package com.sofka.bank.cuentamovimiento.infrastructure.messaging;

public record ClienteCreadoEvent(
        Long clienteId,
        String nombre,
        String identificacion,
        boolean estado
) implements ClienteEventPayload {
}
