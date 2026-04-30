package com.sofka.bank.cuentamovimiento.infrastructure.web.advice;

import com.sofka.bank.cuentamovimiento.domain.exception.CuentaNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.ClienteInactivoException;
import com.sofka.bank.cuentamovimiento.domain.exception.ClienteSnapshotNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.DuplicatedNumeroCuentaException;
import com.sofka.bank.cuentamovimiento.domain.exception.MovimientoAplicadoNoModificableException;
import com.sofka.bank.cuentamovimiento.domain.exception.MovimientoNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.SaldoNoDisponibleException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CuentaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCuentaNotFound(
            CuentaNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(MovimientoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMovimientoNotFound(
            MovimientoNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(ClienteSnapshotNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClienteSnapshotNotFound(
            ClienteSnapshotNotFoundException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), request);
    }

    @ExceptionHandler(DuplicatedNumeroCuentaException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedNumeroCuenta(
            DuplicatedNumeroCuentaException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.CONFLICT, exception.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage() == null ? error.getField() : error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(SaldoNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> handleSaldoNoDisponible(
            SaldoNoDisponibleException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(ClienteInactivoException.class)
    public ResponseEntity<ErrorResponse> handleClienteInactivo(
            ClienteInactivoException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    @ExceptionHandler(MovimientoAplicadoNoModificableException.class)
    public ResponseEntity<ErrorResponse> handleMovimientoAplicadoNoModificable(
            MovimientoAplicadoNoModificableException exception,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), request);
    }

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        return ResponseEntity.status(status)
                .body(new ErrorResponse(
                        LocalDateTime.now(),
                        status.value(),
                        status.getReasonPhrase(),
                        message,
                        request.getRequestURI()
                ));
    }
}
