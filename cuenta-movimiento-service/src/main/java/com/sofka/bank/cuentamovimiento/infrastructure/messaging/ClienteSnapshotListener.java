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
        clienteSnapshotRepository.save(new ClienteSnapshot(
                event.clienteId(),
                event.nombre(),
                event.identificacion(),
                event.estado()
        ));
    }

    @RabbitListener(queues = ClienteMessagingConstants.CLIENTE_ACTUALIZADO_QUEUE)
    public void handleClienteActualizado(ClienteActualizadoEvent event) {
        ClienteSnapshot snapshot = clienteSnapshotRepository.findByClienteId(event.clienteId())
                .orElse(new ClienteSnapshot(
                        event.clienteId(),
                        event.nombre(),
                        event.identificacion(),
                        event.estado()
                ));

        snapshot.actualizar(event.nombre(), event.identificacion(), event.estado());
        clienteSnapshotRepository.save(snapshot);
    }

    @RabbitListener(queues = ClienteMessagingConstants.CLIENTE_DESACTIVADO_QUEUE)
    public void handleClienteDesactivado(ClienteDesactivadoEvent event) {
        ClienteSnapshot snapshot = clienteSnapshotRepository.findByClienteId(event.clienteId())
                .orElse(new ClienteSnapshot(
                        event.clienteId(),
                        event.nombre(),
                        event.identificacion(),
                        event.estado()
                ));

        snapshot.actualizar(event.nombre(), event.identificacion(), event.estado());
        clienteSnapshotRepository.save(snapshot);
    }
}
