package com.sofka.bank.cuentamovimiento.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
public class Movimiento {

    private static final String VALOR_CERO_NO_VALIDO_MESSAGE = "El valor del movimiento no puede ser cero";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    protected Movimiento() {
    }

    public Movimiento(
            Long id,
            LocalDateTime fecha,
            BigDecimal valor,
            BigDecimal saldo,
            Cuenta cuenta
    ) {
        validarValor(valor);

        this.id = id;
        this.fecha = fecha;
        this.tipoMovimiento = clasificarTipo(valor);
        this.valor = valor;
        this.saldo = saldo;
        this.cuenta = cuenta;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    private static void validarValor(BigDecimal valor) {
        if (esValorCero(valor)) {
            throw new IllegalArgumentException(VALOR_CERO_NO_VALIDO_MESSAGE);
        }
    }

    private static TipoMovimiento clasificarTipo(BigDecimal valor) {
        return esDeposito(valor)
                ? TipoMovimiento.DEPOSITO
                : TipoMovimiento.RETIRO;
    }

    private static boolean esValorCero(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) == 0;
    }

    private static boolean esDeposito(BigDecimal valor) {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }
}
