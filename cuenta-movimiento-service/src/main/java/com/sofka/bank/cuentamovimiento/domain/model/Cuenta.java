package com.sofka.bank.cuentamovimiento.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    private static final String SALDO_INICIAL_NEGATIVO_MESSAGE = "El saldo inicial no puede ser negativo";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuenta tipoCuenta;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoInicial;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoDisponible;

    @Column(nullable = false)
    private boolean estado;

    @Column(nullable = false)
    private Long clienteId;

    protected Cuenta() {
    }

    public Cuenta(
            Long id,
            String numeroCuenta,
            TipoCuenta tipoCuenta,
            BigDecimal saldoInicial,
            boolean estado,
            Long clienteId
    ) {
        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        asignarSaldosIniciales(saldoInicial);
        this.estado = estado;
        this.clienteId = clienteId;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public TipoCuenta getTipoCuenta() {
        return tipoCuenta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public boolean isEstado() {
        return estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void actualizar(
            String numeroCuenta,
            TipoCuenta tipoCuenta,
            BigDecimal saldoInicial,
            boolean estado,
            Long clienteId
    ) {
        validarSaldoInicial(saldoInicial);
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.estado = estado;
        this.clienteId = clienteId;
    }

    public void desactivar() {
        this.estado = false;
    }

    public BigDecimal calcularSaldoResultante(BigDecimal valor) {
        return saldoDisponible.add(valor);
    }

    public void actualizarSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    private void asignarSaldosIniciales(BigDecimal saldoInicial) {
        validarSaldoInicial(saldoInicial);
        this.saldoInicial = saldoInicial;
        this.saldoDisponible = saldoInicial;
    }

    private void validarSaldoInicial(BigDecimal saldoInicial) {
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(SALDO_INICIAL_NEGATIVO_MESSAGE);
        }
    }
}
