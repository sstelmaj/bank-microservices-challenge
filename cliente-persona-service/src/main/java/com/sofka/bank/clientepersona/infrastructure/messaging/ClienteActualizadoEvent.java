package com.sofka.bank.clientepersona.infrastructure.messaging;

public record ClienteActualizadoEvent(
        Long clienteId,
        String nombre,
        String identificacion,
        boolean estado
) {
}
