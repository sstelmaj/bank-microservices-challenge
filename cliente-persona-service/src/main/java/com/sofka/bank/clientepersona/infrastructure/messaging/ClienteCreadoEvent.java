package com.sofka.bank.clientepersona.infrastructure.messaging;

public record ClienteCreadoEvent(
        Long clienteId,
        String nombre,
        String identificacion,
        boolean estado
) {
}
