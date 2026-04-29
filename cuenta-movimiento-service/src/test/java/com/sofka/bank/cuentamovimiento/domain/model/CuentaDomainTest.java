package com.sofka.bank.cuentamovimiento.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CuentaDomainTest {

    @Test
    void shouldCreateCuentaWithExpectedAttributes() {
        Cuenta cuenta = new Cuenta(
                null,
                "478758",
                TipoCuenta.AHORRO,
                new BigDecimal("2000.00"),
                true,
                1L
        );

        assertEquals("478758", cuenta.getNumeroCuenta());
        assertEquals(TipoCuenta.AHORRO, cuenta.getTipoCuenta());
        assertEquals(new BigDecimal("2000.00"), cuenta.getSaldoInicial());
        assertEquals(new BigDecimal("2000.00"), cuenta.getSaldoDisponible());
        assertTrue(cuenta.isEstado());
        assertEquals(1L, cuenta.getClienteId());
    }

    @Test
    void shouldInitializeSaldoDisponibleWithSaldoInicial() {
        Cuenta cuenta = new Cuenta(
                null,
                "225487",
                TipoCuenta.CORRIENTE,
                new BigDecimal("100.00"),
                true,
                2L
        );

        assertEquals(cuenta.getSaldoInicial(), cuenta.getSaldoDisponible());
    }

    @Test
    void shouldRejectNegativeSaldoInicial() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Cuenta(
                        null,
                        "495878",
                        TipoCuenta.AHORRO,
                        new BigDecimal("-1.00"),
                        true,
                        3L
                )
        );

        assertEquals("El saldo inicial no puede ser negativo", exception.getMessage());
    }
}
