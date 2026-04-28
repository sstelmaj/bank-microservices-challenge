package com.sofka.bank.clientepersona.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClienteRequest(
        @NotBlank String nombre,
        String genero,
        @NotNull @Min(0) Integer edad,
        @NotBlank String identificacion,
        @NotBlank String direccion,
        @NotBlank String telefono,
        @NotBlank String contrasena,
        @NotNull Boolean estado
) {
}
