package com.sofka.bank.clientepersona.config;

public final class ClienteMessagingConstants {

    public static final String CLIENTES_EXCHANGE = "clientes.exchange";
    public static final String CLIENTE_CREADO_ROUTING_KEY = "cliente.creado";
    public static final String CLIENTE_ACTUALIZADO_ROUTING_KEY = "cliente.actualizado";
    public static final String CLIENTE_DESACTIVADO_ROUTING_KEY = "cliente.desactivado";

    private ClienteMessagingConstants() {
    }
}
