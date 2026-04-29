package com.sofka.bank.clientepersona.infrastructure.messaging;

public interface ClienteEventPublisher {

    void publishClienteCreado(ClienteCreadoEvent event);

    void publishClienteActualizado(ClienteActualizadoEvent event);

    void publishClienteDesactivado(ClienteDesactivadoEvent event);
}
