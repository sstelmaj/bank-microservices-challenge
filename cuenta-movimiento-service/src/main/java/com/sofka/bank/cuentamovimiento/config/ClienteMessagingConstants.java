package com.sofka.bank.cuentamovimiento.config;

public final class ClienteMessagingConstants {

    public static final String CLIENTES_EXCHANGE = "clientes.exchange";
    public static final String CLIENTE_CREADO_ROUTING_KEY = "cliente.creado";
    public static final String CLIENTE_ACTUALIZADO_ROUTING_KEY = "cliente.actualizado";
    public static final String CLIENTE_DESACTIVADO_ROUTING_KEY = "cliente.desactivado";

    public static final String CLIENTE_CREADO_QUEUE = "cuenta-movimiento.cliente-creado.queue";
    public static final String CLIENTE_ACTUALIZADO_QUEUE = "cuenta-movimiento.cliente-actualizado.queue";
    public static final String CLIENTE_DESACTIVADO_QUEUE = "cuenta-movimiento.cliente-desactivado.queue";

    private ClienteMessagingConstants() {
    }
}
