package com.sofka.bank.clientepersona.infrastructure.messaging;

import com.sofka.bank.clientepersona.config.ClienteMessagingConstants;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitClienteEventPublisher implements ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public RabbitClienteEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publishClienteCreado(ClienteCreadoEvent event) {
        rabbitTemplate.convertAndSend(
                ClienteMessagingConstants.CLIENTES_EXCHANGE,
                ClienteMessagingConstants.CLIENTE_CREADO_ROUTING_KEY,
                event
        );
    }

    @Override
    public void publishClienteActualizado(ClienteActualizadoEvent event) {
        rabbitTemplate.convertAndSend(
                ClienteMessagingConstants.CLIENTES_EXCHANGE,
                ClienteMessagingConstants.CLIENTE_ACTUALIZADO_ROUTING_KEY,
                event
        );
    }

    @Override
    public void publishClienteDesactivado(ClienteDesactivadoEvent event) {
        rabbitTemplate.convertAndSend(
                ClienteMessagingConstants.CLIENTES_EXCHANGE,
                ClienteMessagingConstants.CLIENTE_DESACTIVADO_ROUTING_KEY,
                event
        );
    }
}
