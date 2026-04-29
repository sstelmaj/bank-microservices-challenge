package com.sofka.bank.cuentamovimiento.infrastructure.messaging;

public record ClienteDesactivadoEvent(
        Long clienteId,
        String nombre,
        String identificacion,
        boolean estado
) implements ClienteEventPayload {
}
