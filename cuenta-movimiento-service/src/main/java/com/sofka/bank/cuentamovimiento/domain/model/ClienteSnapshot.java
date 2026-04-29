package com.sofka.bank.cuentamovimiento.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cliente_snapshot")
public class ClienteSnapshot {

    @Id
    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String identificacion;

    @Column(nullable = false)
    private boolean estado;

    protected ClienteSnapshot() {
    }

    public ClienteSnapshot(Long clienteId, String nombre, String identificacion, boolean estado) {
        this.clienteId = clienteId;
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.estado = estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void actualizar(String nombre, String identificacion, boolean estado) {
        this.nombre = nombre;
        this.identificacion = identificacion;
        this.estado = estado;
    }
}
