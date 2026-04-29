package com.sofka.bank.clientepersona.infrastructure.messaging;

public record ClienteDesactivadoEvent(
        Long clienteId,
        String nombre,
        String identificacion,
        boolean estado
) {
}
