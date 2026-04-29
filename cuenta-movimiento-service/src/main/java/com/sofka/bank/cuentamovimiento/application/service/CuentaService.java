package com.sofka.bank.cuentamovimiento.application.service;

import com.sofka.bank.cuentamovimiento.application.dto.CuentaRequest;
import com.sofka.bank.cuentamovimiento.application.dto.CuentaResponse;
import com.sofka.bank.cuentamovimiento.domain.exception.CuentaNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.DuplicatedNumeroCuentaException;
import com.sofka.bank.cuentamovimiento.domain.model.Cuenta;
import com.sofka.bank.cuentamovimiento.domain.repository.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CuentaService {

    private final CuentaRepository cuentaRepository;

    public CuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public CuentaResponse crear(CuentaRequest request) {
        validarNumeroCuentaDuplicado(request.numeroCuenta(), null);

        Cuenta cuenta = new Cuenta(
                null,
                request.numeroCuenta(),
                request.tipoCuenta(),
                request.saldoInicial(),
                request.estado(),
                request.clienteId()
        );

        return toResponse(cuentaRepository.save(cuenta));
    }

    @Transactional(readOnly = true)
    public List<CuentaResponse> listar() {
        return cuentaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CuentaResponse obtenerPorId(Long id) {
        return toResponse(buscarCuenta(id));
    }

    public CuentaResponse actualizar(Long id, CuentaRequest request) {
        Cuenta cuenta = buscarCuenta(id);
        validarNumeroCuentaDuplicado(request.numeroCuenta(), id);

        cuenta.actualizar(
                request.numeroCuenta(),
                request.tipoCuenta(),
                request.saldoInicial(),
                request.estado(),
                request.clienteId()
        );

        return toResponse(cuentaRepository.save(cuenta));
    }

    public void desactivar(Long id) {
        Cuenta cuenta = buscarCuenta(id);
        cuenta.desactivar();
        cuentaRepository.save(cuenta);
    }

    private Cuenta buscarCuenta(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada"));
    }

    private void validarNumeroCuentaDuplicado(String numeroCuenta, Long cuentaId) {
        cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .filter(cuenta -> perteneceAOtraCuenta(cuenta, cuentaId))
                .ifPresent(cuenta -> {
                    throw new DuplicatedNumeroCuentaException(
                            "Ya existe una cuenta con el numero " + numeroCuenta
                    );
                });
    }

    private boolean perteneceAOtraCuenta(Cuenta cuenta, Long cuentaId) {
        return cuentaId == null || !cuenta.getId().equals(cuentaId);
    }

    private CuentaResponse toResponse(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.isEstado(),
                cuenta.getClienteId()
        );
    }
}
