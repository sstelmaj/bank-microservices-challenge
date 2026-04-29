package com.sofka.bank.cuentamovimiento.domain.repository;

import com.sofka.bank.cuentamovimiento.domain.model.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
}
