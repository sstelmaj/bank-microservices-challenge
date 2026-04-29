package com.sofka.bank.cuentamovimiento.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class RabbitMessagingConfig {

    @Bean
    public TopicExchange clientesExchange() {
        return new TopicExchange(ClienteMessagingConstants.CLIENTES_EXCHANGE);
    }

    @Bean
    public Queue clienteCreadoQueue() {
        return new Queue(ClienteMessagingConstants.CLIENTE_CREADO_QUEUE);
    }

    @Bean
    public Queue clienteActualizadoQueue() {
        return new Queue(ClienteMessagingConstants.CLIENTE_ACTUALIZADO_QUEUE);
    }

    @Bean
    public Queue clienteDesactivadoQueue() {
        return new Queue(ClienteMessagingConstants.CLIENTE_DESACTIVADO_QUEUE);
    }

    @Bean
    public Binding clienteCreadoBinding(
            @Qualifier("clienteCreadoQueue") Queue clienteCreadoQueue,
            TopicExchange clientesExchange) {
        return BindingBuilder.bind(clienteCreadoQueue)
                .to(clientesExchange)
                .with(ClienteMessagingConstants.CLIENTE_CREADO_ROUTING_KEY);
    }

    @Bean
    public Binding clienteActualizadoBinding(
            @Qualifier("clienteActualizadoQueue") Queue clienteActualizadoQueue,
            TopicExchange clientesExchange) {
        return BindingBuilder.bind(clienteActualizadoQueue)
                .to(clientesExchange)
                .with(ClienteMessagingConstants.CLIENTE_ACTUALIZADO_ROUTING_KEY);
    }

    @Bean
    public Binding clienteDesactivadoBinding(
            @Qualifier("clienteDesactivadoQueue") Queue clienteDesactivadoQueue,
            TopicExchange clientesExchange) {
        return BindingBuilder.bind(clienteDesactivadoQueue)
                .to(clientesExchange)
                .with(ClienteMessagingConstants.CLIENTE_DESACTIVADO_ROUTING_KEY);
    }

    @Bean
    public MessageConverter rabbitMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
