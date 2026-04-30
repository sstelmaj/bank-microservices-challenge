package com.sofka.bank.cuentamovimiento.application.service;

import com.sofka.bank.cuentamovimiento.application.dto.MovimientoRequest;
import com.sofka.bank.cuentamovimiento.application.dto.MovimientoResponse;
import com.sofka.bank.cuentamovimiento.application.mapper.MovimientoMapper;
import com.sofka.bank.cuentamovimiento.domain.exception.CuentaNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.MovimientoAplicadoNoModificableException;
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
@Transactional(readOnly = true)
public class MovimientoService {

    private static final String SALDO_NO_DISPONIBLE_MESSAGE = "Saldo no disponible";
    private static final String MOVIMIENTO_NO_MODIFICABLE_MESSAGE =
            "Los movimientos aplicados no pueden modificarse por integridad transaccional";
    private static final String MOVIMIENTO_NO_ELIMINABLE_MESSAGE =
            "Los movimientos aplicados no pueden eliminarse por integridad transaccional";

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
        BigDecimal saldoResultante = aplicarMovimientoEnCuenta(cuenta, request.valor());
        Movimiento movimiento = crearMovimiento(request, cuenta, saldoResultante);

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
        return movimientoMapper.toResponse(buscarMovimiento(id));
    }

    @Transactional(readOnly = true)
    public void rechazarActualizacion(Long id) {
        buscarMovimiento(id);
        throw new MovimientoAplicadoNoModificableException(MOVIMIENTO_NO_MODIFICABLE_MESSAGE);
    }

    @Transactional(readOnly = true)
    public void rechazarEliminacion(Long id) {
        buscarMovimiento(id);
        throw new MovimientoAplicadoNoModificableException(MOVIMIENTO_NO_ELIMINABLE_MESSAGE);
    }

    private Cuenta buscarCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada"));
    }

    private Movimiento buscarMovimiento(Long id) {
        return movimientoRepository.findById(id)
                .orElseThrow(() -> new MovimientoNotFoundException("Movimiento no encontrado"));
    }

    private BigDecimal aplicarMovimientoEnCuenta(Cuenta cuenta, BigDecimal valor) {
        BigDecimal saldoResultante = cuenta.calcularSaldoResultante(valor);
        validarSaldoDisponible(saldoResultante);
        cuenta.actualizarSaldoDisponible(saldoResultante);
        return saldoResultante;
    }

    private Movimiento crearMovimiento(MovimientoRequest request, Cuenta cuenta, BigDecimal saldoResultante) {
        return movimientoMapper.toEntity(
                request,
                cuenta,
                saldoResultante,
                LocalDateTime.now()
        );
    }

    private void validarSaldoDisponible(BigDecimal saldoResultante) {
        if (saldoResultante.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException(SALDO_NO_DISPONIBLE_MESSAGE);
        }
    }
}
