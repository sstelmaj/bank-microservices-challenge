package com.sofka.bank.cuentamovimiento.infrastructure.web.controller;

import com.sofka.bank.cuentamovimiento.application.dto.CuentaRequest;
import com.sofka.bank.cuentamovimiento.application.dto.CuentaResponse;
import com.sofka.bank.cuentamovimiento.application.service.CuentaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @PostMapping
    public ResponseEntity<CuentaResponse> crear(@Valid @RequestBody CuentaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaService.crear(request));
    }

    @GetMapping
    public List<CuentaResponse> listar() {
        return cuentaService.listar();
    }

    @GetMapping("/{id}")
    public CuentaResponse obtenerPorId(@PathVariable("id") Long id) {
        return cuentaService.obtenerPorId(id);
    }

    @PutMapping("/{id}")
    public CuentaResponse actualizar(@PathVariable("id") Long id, @Valid @RequestBody CuentaRequest request) {
        return cuentaService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable("id") Long id) {
        cuentaService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
