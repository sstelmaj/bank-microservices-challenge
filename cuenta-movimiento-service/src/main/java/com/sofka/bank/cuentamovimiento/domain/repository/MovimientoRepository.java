package com.sofka.bank.cuentamovimiento.domain.repository;

import com.sofka.bank.cuentamovimiento.domain.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findAllByOrderByIdAsc();

    List<Movimiento> findAllByCuentaIdAndFechaBetweenOrderByFechaAscIdAsc(
            Long cuentaId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );
}
