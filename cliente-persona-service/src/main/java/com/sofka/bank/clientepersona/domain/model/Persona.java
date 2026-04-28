package com.sofka.bank.clientepersona.domain.model;

public class Persona {

    private final String nombre;
    private final String genero;
    private final Integer edad;
    private final String identificacion;
    private final String direccion;
    private final String telefono;

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
