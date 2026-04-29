package com.sofka.bank.cuentamovimiento.infrastructure.web.controller;

import com.sofka.bank.cuentamovimiento.application.dto.ReporteEstadoCuentaResponse;
import com.sofka.bank.cuentamovimiento.application.service.ReporteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping
    public ResponseEntity<ReporteEstadoCuentaResponse> obtenerEstadoCuenta(
            @RequestParam("clienteId") Long clienteId,
            @RequestParam("fecha") String fecha
    ) {
        return ResponseEntity.ok(reporteService.generarEstadoCuenta(clienteId, fecha));
    }
}
