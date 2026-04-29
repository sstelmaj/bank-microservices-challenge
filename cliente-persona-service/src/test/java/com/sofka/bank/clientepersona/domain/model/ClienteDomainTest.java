package com.sofka.bank.clientepersona.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteDomainTest {

    @Test
    void shouldCreateAnActiveClientWithPersonaData() {
        Cliente cliente = new Cliente(
                1L,
                "Jose Lema",
                "MASCULINO",
                35,
                "1234567890",
                "Otavalo sn y principal",
                "098254785",
                "1234",
                true
        );

        assertThat(cliente.getClienteId()).isEqualTo(1L);
        assertThat(cliente.getNombre()).isEqualTo("Jose Lema");
        assertThat(cliente.getGenero()).isEqualTo("MASCULINO");
        assertThat(cliente.getEdad()).isEqualTo(35);
        assertThat(cliente.getIdentificacion()).isEqualTo("1234567890");
        assertThat(cliente.getDireccion()).isEqualTo("Otavalo sn y principal");
        assertThat(cliente.getTelefono()).isEqualTo("098254785");
        assertThat(cliente.getContrasena()).isEqualTo("1234");
        assertThat(cliente.isEstado()).isTrue();
    }
}
