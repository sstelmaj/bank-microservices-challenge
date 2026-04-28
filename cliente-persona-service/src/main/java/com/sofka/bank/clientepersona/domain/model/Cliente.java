package com.sofka.bank.clientepersona.domain.model;

public class Cliente extends Persona {

    private final Long clienteId;
    private final String contrasena;
    private final boolean estado;

    public Cliente(
            Long clienteId,
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String contrasena,
            boolean estado
    ) {
        super(nombre, genero, edad, identificacion, direccion, telefono);
        this.clienteId = clienteId;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public boolean isEstado() {
        return estado;
    }
}
