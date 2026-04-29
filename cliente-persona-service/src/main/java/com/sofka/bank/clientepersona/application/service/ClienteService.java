package com.sofka.bank.clientepersona.application.service;

import com.sofka.bank.clientepersona.application.dto.ClienteRequest;
import com.sofka.bank.clientepersona.application.dto.ClienteResponse;
import com.sofka.bank.clientepersona.application.mapper.ClienteMapper;
import com.sofka.bank.clientepersona.domain.exception.ClienteNotFoundException;
import com.sofka.bank.clientepersona.domain.exception.DuplicatedIdentificacionException;
import com.sofka.bank.clientepersona.domain.model.Cliente;
import com.sofka.bank.clientepersona.domain.repository.ClienteRepository;
import com.sofka.bank.clientepersona.infrastructure.messaging.ClienteActualizadoEvent;
import com.sofka.bank.clientepersona.infrastructure.messaging.ClienteCreadoEvent;
import com.sofka.bank.clientepersona.infrastructure.messaging.ClienteDesactivadoEvent;
import com.sofka.bank.clientepersona.infrastructure.messaging.ClienteEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteEventPublisher clienteEventPublisher;

    public ClienteService(
            ClienteRepository clienteRepository,
            ClienteMapper clienteMapper,
            ClienteEventPublisher clienteEventPublisher
    ) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
        this.clienteEventPublisher = clienteEventPublisher;
    }

    public ClienteResponse crear(ClienteRequest request) {
        validarIdentificacionDuplicada(request.identificacion(), null);
        Cliente cliente = clienteMapper.toEntity(request);
        Cliente savedCliente = clienteRepository.save(cliente);
        clienteEventPublisher.publishClienteCreado(toClienteCreadoEvent(savedCliente));
        return clienteMapper.toResponse(savedCliente);
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
        Cliente savedCliente = clienteRepository.save(cliente);
        clienteEventPublisher.publishClienteActualizado(toClienteActualizadoEvent(savedCliente));
        return clienteMapper.toResponse(savedCliente);
    }

    public void desactivar(Long clienteId) {
        Cliente cliente = buscarCliente(clienteId);
        cliente.desactivar();
        Cliente savedCliente = clienteRepository.save(cliente);
        clienteEventPublisher.publishClienteDesactivado(toClienteDesactivadoEvent(savedCliente));
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

    private ClienteCreadoEvent toClienteCreadoEvent(Cliente cliente) {
        return new ClienteCreadoEvent(
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.isEstado()
        );
    }

    private ClienteActualizadoEvent toClienteActualizadoEvent(Cliente cliente) {
        return new ClienteActualizadoEvent(
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.isEstado()
        );
    }

    private ClienteDesactivadoEvent toClienteDesactivadoEvent(Cliente cliente) {
        return new ClienteDesactivadoEvent(
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.isEstado()
        );
    }
}
