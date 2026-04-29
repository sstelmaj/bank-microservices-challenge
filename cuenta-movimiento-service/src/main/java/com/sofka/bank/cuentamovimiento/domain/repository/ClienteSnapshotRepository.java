package com.sofka.bank.cuentamovimiento.domain.repository;

import com.sofka.bank.cuentamovimiento.domain.model.ClienteSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteSnapshotRepository extends JpaRepository<ClienteSnapshot, Long> {

    Optional<ClienteSnapshot> findByClienteId(Long clienteId);
}
