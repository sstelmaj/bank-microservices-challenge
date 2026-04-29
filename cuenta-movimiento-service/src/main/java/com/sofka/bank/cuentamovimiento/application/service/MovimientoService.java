package com.sofka.bank.cuentamovimiento.application.service;

import com.sofka.bank.cuentamovimiento.application.dto.MovimientoRequest;
import com.sofka.bank.cuentamovimiento.application.dto.MovimientoResponse;
import com.sofka.bank.cuentamovimiento.application.mapper.MovimientoMapper;
import com.sofka.bank.cuentamovimiento.domain.exception.CuentaNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.MovimientoNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.SaldoNoDisponibleException;
import com.sofka.bank.cuentamovimiento.domain.model.Cuenta;
import com.sofka.bank.cuentamovimiento.domain.model.Movimiento;
import com.sofka.bank.cuentamovimiento.domain.repository.CuentaRepository;
import com.sofka.bank.cuentamovimiento.domain.repository.MovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoService {

    private static final String SALDO_NO_DISPONIBLE_MESSAGE = "Saldo no disponible";

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoMapper movimientoMapper;

    public MovimientoService(
            MovimientoRepository movimientoRepository,
            CuentaRepository cuentaRepository,
            MovimientoMapper movimientoMapper
    ) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
        this.movimientoMapper = movimientoMapper;
    }

    @Transactional
    public MovimientoResponse registrar(MovimientoRequest request) {
        Cuenta cuenta = buscarCuentaPorNumero(request.numeroCuenta());
        BigDecimal saldoResultante = cuenta.calcularSaldoResultante(request.valor());

        validarSaldoDisponible(saldoResultante);
        cuenta.actualizarSaldoDisponible(saldoResultante);

        Movimiento movimiento = movimientoMapper.toEntity(
                request,
                cuenta,
                saldoResultante,
                LocalDateTime.now()
        );

        cuentaRepository.save(cuenta);
        return movimientoMapper.toResponse(movimientoRepository.save(movimiento));
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listar() {
        return movimientoRepository.findAllByOrderByIdAsc().stream()
                .map(movimientoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimientoResponse obtenerPorId(Long id) {
        return movimientoMapper.toResponse(
                movimientoRepository.findById(id)
                        .orElseThrow(() -> new MovimientoNotFoundException("Movimiento no encontrado"))
        );
    }

    private Cuenta buscarCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada"));
    }

    private void validarSaldoDisponible(BigDecimal saldoResultante) {
        if (saldoResultante.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException(SALDO_NO_DISPONIBLE_MESSAGE);
        }
    }
}
