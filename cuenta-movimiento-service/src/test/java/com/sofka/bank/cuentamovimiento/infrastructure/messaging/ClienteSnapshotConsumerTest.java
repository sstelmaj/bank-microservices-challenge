package com.sofka.bank.cuentamovimiento.infrastructure.messaging;

import com.sofka.bank.cuentamovimiento.domain.model.ClienteSnapshot;
import com.sofka.bank.cuentamovimiento.domain.repository.ClienteSnapshotRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = {
        "spring.jpa.open-in-view=false"
})
class ClienteSnapshotConsumerTest {

    @Autowired
    private ClienteSnapshotListener clienteSnapshotListener;

    @Autowired
    private ClienteSnapshotRepository clienteSnapshotRepository;

    @Test
    void shouldStoreSnapshotWhenClienteCreadoIsConsumed() {
        clienteSnapshotListener.handleClienteCreado(new ClienteCreadoEvent(1L, "Jose Lema", "1234567890", true));

        ClienteSnapshot snapshot = clienteSnapshotRepository.findByClienteId(1L).orElseThrow();
        assertThat(snapshot.getClienteId()).isEqualTo(1L);
        assertThat(snapshot.getNombre()).isEqualTo("Jose Lema");
        assertThat(snapshot.getIdentificacion()).isEqualTo("1234567890");
        assertThat(snapshot.isEstado()).isTrue();
    }

    @Test
    void shouldUpdateSnapshotWhenClienteActualizadoIsConsumed() {
        clienteSnapshotRepository.save(new ClienteSnapshot(1L, "Jose Lema", "1234567890", true));

        clienteSnapshotListener.handleClienteActualizado(
                new ClienteActualizadoEvent(1L, "Jose Lema Actualizado", "1234567890", true)
        );

        ClienteSnapshot snapshot = clienteSnapshotRepository.findByClienteId(1L).orElseThrow();
        assertThat(snapshot.getNombre()).isEqualTo("Jose Lema Actualizado");
        assertThat(snapshot.isEstado()).isTrue();
    }

    @Test
    void shouldMarkSnapshotInactiveWhenClienteDesactivadoIsConsumed() {
        clienteSnapshotRepository.save(new ClienteSnapshot(1L, "Jose Lema", "1234567890", true));

        clienteSnapshotListener.handleClienteDesactivado(
                new ClienteDesactivadoEvent(1L, "Jose Lema", "1234567890", false)
        );

        ClienteSnapshot snapshot = clienteSnapshotRepository.findByClienteId(1L).orElseThrow();
        assertThat(snapshot.isEstado()).isFalse();
    }
}
