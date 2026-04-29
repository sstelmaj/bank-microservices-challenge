package com.sofka.bank.cuentamovimiento.infrastructure.web.controller;

import com.sofka.bank.cuentamovimiento.application.dto.MovimientoRequest;
import com.sofka.bank.cuentamovimiento.application.dto.MovimientoResponse;
import com.sofka.bank.cuentamovimiento.application.service.MovimientoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @PostMapping
    public ResponseEntity<MovimientoResponse> registrar(@Valid @RequestBody MovimientoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(movimientoService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponse>> listar() {
        return ResponseEntity.ok(movimientoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovimientoResponse> obtenerPorId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(movimientoService.obtenerPorId(id));
    }
}
