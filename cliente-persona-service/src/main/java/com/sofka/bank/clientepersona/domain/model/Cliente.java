package com.sofka.bank.clientepersona.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente extends Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private String contrasena;

    @Column(nullable = false)
    private boolean estado;

    protected Cliente() {
    }

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

    public void actualizar(
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono,
            String contrasena,
            boolean estado
    ) {
        actualizarPersona(nombre, genero, edad, identificacion, direccion, telefono);
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public void desactivar() {
        this.estado = false;
    }
}
