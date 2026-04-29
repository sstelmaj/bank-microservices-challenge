package com.sofka.bank.cuentamovimiento.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MovimientoDomainTest {

    @Test
    void shouldClassifyPositiveValueAsDeposito() {
        Movimiento movimiento = new Movimiento(
                null,
                LocalDateTime.of(2022, 2, 10, 10, 30),
                new BigDecimal("600.00"),
                new BigDecimal("700.00"),
                createCuenta("225487")
        );

        assertEquals(TipoMovimiento.DEPOSITO, movimiento.getTipoMovimiento());
    }

    @Test
    void shouldClassifyNegativeValueAsRetiro() {
        Movimiento movimiento = new Movimiento(
                null,
                LocalDateTime.of(2022, 2, 8, 9, 15),
                new BigDecimal("-540.00"),
                new BigDecimal("0.00"),
                createCuenta("496825")
        );

        assertEquals(TipoMovimiento.RETIRO, movimiento.getTipoMovimiento());
    }

    @Test
    void shouldRejectZeroValue() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Movimiento(
                        null,
                        LocalDateTime.of(2022, 2, 10, 10, 30),
                        BigDecimal.ZERO,
                        new BigDecimal("700.00"),
                        createCuenta("225487")
                )
        );

        assertEquals("El valor del movimiento no puede ser cero", exception.getMessage());
    }

    @Test
    void shouldPreserveFechaValorTipoAndSaldoResultante() {
        LocalDateTime fecha = LocalDateTime.of(2022, 2, 10, 10, 30);
        Movimiento movimiento = new Movimiento(
                null,
                fecha,
                new BigDecimal("600.00"),
                new BigDecimal("700.00"),
                createCuenta("225487")
        );

        assertEquals(fecha, movimiento.getFecha());
        assertEquals(new BigDecimal("600.00"), movimiento.getValor());
        assertEquals(TipoMovimiento.DEPOSITO, movimiento.getTipoMovimiento());
        assertEquals(new BigDecimal("700.00"), movimiento.getSaldo());
        assertEquals("225487", movimiento.getCuenta().getNumeroCuenta());
    }

    private Cuenta createCuenta(String numeroCuenta) {
        return new Cuenta(
                1L,
                numeroCuenta,
                TipoCuenta.AHORRO,
                new BigDecimal("100.00"),
                true,
                1L
        );
    }
}
