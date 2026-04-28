package com.sofka.bank.clientepersona.application.service;

import com.sofka.bank.clientepersona.application.dto.ClienteRequest;
import com.sofka.bank.clientepersona.application.dto.ClienteResponse;
import com.sofka.bank.clientepersona.application.mapper.ClienteMapper;
import com.sofka.bank.clientepersona.domain.exception.ClienteNotFoundException;
import com.sofka.bank.clientepersona.domain.exception.DuplicatedIdentificacionException;
import com.sofka.bank.clientepersona.domain.model.Cliente;
import com.sofka.bank.clientepersona.domain.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public ClienteResponse crear(ClienteRequest request) {
        validarIdentificacionDuplicada(request.identificacion(), null);
        Cliente cliente = clienteMapper.toEntity(request);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse obtenerPorId(Long clienteId) {
        return clienteMapper.toResponse(buscarCliente(clienteId));
    }

    public ClienteResponse actualizar(Long clienteId, ClienteRequest request) {
        Cliente cliente = buscarCliente(clienteId);
        validarIdentificacionDuplicada(request.identificacion(), clienteId);
        clienteMapper.updateEntity(cliente, request);
        return clienteMapper.toResponse(clienteRepository.save(cliente));
    }

    public void desactivar(Long clienteId) {
        Cliente cliente = buscarCliente(clienteId);
        cliente.desactivar();
        clienteRepository.save(cliente);
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado"));
    }

    private void validarIdentificacionDuplicada(String identificacion, Long clienteId) {
        clienteRepository.findByIdentificacion(identificacion)
                .filter(cliente -> perteneceAOtroCliente(cliente, clienteId))
                .ifPresent(cliente -> {
                    throw new DuplicatedIdentificacionException(
                            "Ya existe un cliente con la identificacion " + identificacion
                    );
                });
    }

    private boolean perteneceAOtroCliente(Cliente cliente, Long clienteId) {
        return clienteId == null || !cliente.getClienteId().equals(clienteId);
    }
}
