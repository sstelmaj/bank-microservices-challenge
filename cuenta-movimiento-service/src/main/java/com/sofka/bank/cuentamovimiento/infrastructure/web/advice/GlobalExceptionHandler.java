package com.sofka.bank.cuentamovimiento.infrastructure.web.advice;

import com.sofka.bank.cuentamovimiento.domain.exception.CuentaNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.ClienteInactivoException;
import com.sofka.bank.cuentamovimiento.domain.exception.ClienteSnapshotNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.DuplicatedNumeroCuentaException;
import com.sofka.bank.cuentamovimiento.domain.exception.MovimientoAplicadoNoModificableException;
import com.sofka.bank.cuentamovimiento.domain.exception.MovimientoNotFoundException;
import com.sofka.bank.cuentamovimiento.domain.exception.SaldoNoDisponibleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CuentaNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCuentaNotFound(CuentaNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MovimientoNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMovimientoNotFound(MovimientoNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(ClienteSnapshotNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClienteSnapshotNotFound(ClienteSnapshotNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(DuplicatedNumeroCuentaException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedNumeroCuenta(DuplicatedNumeroCuentaException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage() == null ? error.getField() : error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(SaldoNoDisponibleException.class)
    public ResponseEntity<ErrorResponse> handleSaldoNoDisponible(SaldoNoDisponibleException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(ClienteInactivoException.class)
    public ResponseEntity<ErrorResponse> handleClienteInactivo(ClienteInactivoException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MovimientoAplicadoNoModificableException.class)
    public ResponseEntity<ErrorResponse> handleMovimientoAplicadoNoModificable(
            MovimientoAplicadoNoModificableException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }
}
