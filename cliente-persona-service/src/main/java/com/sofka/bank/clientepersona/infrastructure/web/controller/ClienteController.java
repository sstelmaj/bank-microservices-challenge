package com.sofka.bank.clientepersona.infrastructure.web.controller;

import com.sofka.bank.clientepersona.application.dto.ClienteRequest;
import com.sofka.bank.clientepersona.application.dto.ClienteResponse;
import com.sofka.bank.clientepersona.application.service.ClienteService;
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
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody ClienteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteService.crear(request));
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{clienteId}")
    public ClienteResponse obtenerPorId(@PathVariable("clienteId") Long clienteId) {
        return clienteService.obtenerPorId(clienteId);
    }

    @PutMapping("/{clienteId}")
    public ClienteResponse actualizar(@PathVariable("clienteId") Long clienteId, @Valid @RequestBody ClienteRequest request) {
        return clienteService.actualizar(clienteId, request);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> desactivar(@PathVariable("clienteId") Long clienteId) {
        clienteService.desactivar(clienteId);
        return ResponseEntity.noContent().build();
    }
}
