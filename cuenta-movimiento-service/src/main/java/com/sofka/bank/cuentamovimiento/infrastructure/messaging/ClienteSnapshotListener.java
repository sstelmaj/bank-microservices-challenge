package com.sofka.bank.cuentamovimiento.infrastructure.messaging;

import com.sofka.bank.cuentamovimiento.config.ClienteMessagingConstants;
import com.sofka.bank.cuentamovimiento.domain.model.ClienteSnapshot;
import com.sofka.bank.cuentamovimiento.domain.repository.ClienteSnapshotRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ClienteSnapshotListener {

    private final ClienteSnapshotRepository clienteSnapshotRepository;

    public ClienteSnapshotListener(ClienteSnapshotRepository clienteSnapshotRepository) {
        this.clienteSnapshotRepository = clienteSnapshotRepository;
    }

    @RabbitListener(queues = ClienteMessagingConstants.CLIENTE_CREADO_QUEUE)
    public void handleClienteCreado(ClienteCreadoEvent event) {
        guardarSnapshot(event);
    }

    @RabbitListener(queues = ClienteMessagingConstants.CLIENTE_ACTUALIZADO_QUEUE)
    public void handleClienteActualizado(ClienteActualizadoEvent event) {
        actualizarSnapshot(event);
    }

    @RabbitListener(queues = ClienteMessagingConstants.CLIENTE_DESACTIVADO_QUEUE)
    public void handleClienteDesactivado(ClienteDesactivadoEvent event) {
        actualizarSnapshot(event);
    }

    private void guardarSnapshot(ClienteEventPayload event) {
        clienteSnapshotRepository.save(crearSnapshot(event));
    }

    private void actualizarSnapshot(ClienteEventPayload event) {
        ClienteSnapshot snapshot = clienteSnapshotRepository.findByClienteId(event.clienteId())
                .orElseGet(() -> crearSnapshot(event));

        snapshot.actualizar(event.nombre(), event.identificacion(), event.estado());
        clienteSnapshotRepository.save(snapshot);
    }

    private ClienteSnapshot crearSnapshot(ClienteEventPayload event) {
        return new ClienteSnapshot(
                event.clienteId(),
                event.nombre(),
                event.identificacion(),
                event.estado()
        );
    }
}
