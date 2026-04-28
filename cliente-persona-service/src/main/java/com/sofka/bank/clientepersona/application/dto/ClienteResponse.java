package com.sofka.bank.clientepersona.application.dto;

public record ClienteResponse(
        Long clienteId,
        String nombre,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        boolean estado
) {
}
