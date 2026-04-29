package com.sofka.bank.clientepersona.application.service;

import com.sofka.bank.clientepersona.application.dto.ClienteRequest;
import com.sofka.bank.clientepersona.application.mapper.ClienteMapper;
import com.sofka.bank.clientepersona.domain.model.Cliente;
import com.sofka.bank.clientepersona.domain.repository.ClienteRepository;
import com.sofka.bank.clientepersona.infrastructure.messaging.ClienteCreadoEvent;
import com.sofka.bank.clientepersona.infrastructure.messaging.ClienteActualizadoEvent;
import com.sofka.bank.clientepersona.infrastructure.messaging.ClienteDesactivadoEvent;
import com.sofka.bank.clientepersona.infrastructure.messaging.ClienteEventPublisher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteEventPublicationTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @Mock
    private ClienteEventPublisher clienteEventPublisher;

    @InjectMocks
    private ClienteService clienteService;

    @Captor
    private ArgumentCaptor<ClienteCreadoEvent> clienteCreadoEventCaptor;

    @Captor
    private ArgumentCaptor<ClienteActualizadoEvent> clienteActualizadoEventCaptor;

    @Captor
    private ArgumentCaptor<ClienteDesactivadoEvent> clienteDesactivadoEventCaptor;

    @Test
    void shouldPublishClienteCreadoWhenClienteIsCreated() {
        ClienteRequest request = validRequest();
        Cliente cliente = clienteActivo(1L);

        when(clienteRepository.findByIdentificacion(request.identificacion())).thenReturn(Optional.empty());
        when(clienteMapper.toEntity(request)).thenReturn(clienteActivo(null));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(null);

        clienteService.crear(request);

        verify(clienteEventPublisher).publishClienteCreado(clienteCreadoEventCaptor.capture());
        assertThat(clienteCreadoEventCaptor.getValue()).isEqualTo(new ClienteCreadoEvent(
                1L,
                "Jose Lema",
                "1234567890",
                true
        ));
    }

    @Test
    void shouldPublishClienteActualizadoWhenClienteIsUpdated() {
        ClienteRequest request = validRequest();
        Cliente cliente = clienteActivo(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.findByIdentificacion(request.identificacion())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toResponse(cliente)).thenReturn(null);

        clienteService.actualizar(1L, request);

        verify(clienteEventPublisher).publishClienteActualizado(clienteActualizadoEventCaptor.capture());
        assertThat(clienteActualizadoEventCaptor.getValue()).isEqualTo(new ClienteActualizadoEvent(
                1L,
                "Jose Lema",
                "1234567890",
                true
        ));
    }

    @Test
    void shouldPublishClienteDesactivadoWhenClienteIsDeactivated() {
        Cliente cliente = clienteActivo(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        clienteService.desactivar(1L);

        verify(clienteEventPublisher).publishClienteDesactivado(clienteDesactivadoEventCaptor.capture());
        assertThat(clienteDesactivadoEventCaptor.getValue()).isEqualTo(new ClienteDesactivadoEvent(
                1L,
                "Jose Lema",
                "1234567890",
                false
        ));
    }

    private ClienteRequest validRequest() {
        return new ClienteRequest(
                "Jose Lema",
                "MASCULINO",
                35,
                "1234567890",
                "Otavalo sn y principal",
                "098254785",
                "1234",
                true
        );
    }

    private Cliente clienteActivo(Long clienteId) {
        return new Cliente(
                clienteId,
                "Jose Lema",
                "MASCULINO",
                35,
                "1234567890",
                "Otavalo sn y principal",
                "098254785",
                "1234",
                true
        );
    }
}
