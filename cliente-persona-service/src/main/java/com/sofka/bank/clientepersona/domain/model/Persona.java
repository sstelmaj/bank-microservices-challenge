package com.sofka.bank.clientepersona.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Persona {

    @Column(nullable = false)
    private String nombre;

    @Column
    private String genero;

    @Column
    private Integer edad;

    @Column(nullable = false, unique = true)
    private String identificacion;

    @Column
    private String direccion;

    @Column
    private String telefono;

    protected Persona() {
    }

    public Persona(
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono
    ) {
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    protected void actualizarPersona(
            String nombre,
            String genero,
            Integer edad,
            String identificacion,
            String direccion,
            String telefono
    ) {
        this.nombre = nombre;
        this.genero = genero;
        this.edad = edad;
        this.identificacion = identificacion;
        this.direccion = direccion;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getGenero() {
        return genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefono() {
        return telefono;
    }
}
