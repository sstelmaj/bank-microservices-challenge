package com.sofka.bank.cuentamovimiento.infrastructure.web.controller;

import com.sofka.bank.cuentamovimiento.domain.model.ClienteSnapshot;
import com.sofka.bank.cuentamovimiento.domain.model.Cuenta;
import com.sofka.bank.cuentamovimiento.domain.model.Movimiento;
import com.sofka.bank.cuentamovimiento.domain.model.TipoCuenta;
import com.sofka.bank.cuentamovimiento.domain.repository.ClienteSnapshotRepository;
import com.sofka.bank.cuentamovimiento.domain.repository.CuentaRepository;
import com.sofka.bank.cuentamovimiento.domain.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = {
        "spring.jpa.open-in-view=false"
})
class ReportesTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ClienteSnapshotRepository clienteSnapshotRepository;

    @BeforeEach
    void setUpSnapshots() {
        clienteSnapshotRepository.save(new ClienteSnapshot(2L, "Marianela Montalvo", "0987654321", true));
    }

    @Test
    void getReportesShouldReturnClientAccountsWithBalances() throws Exception {
        createCuenta(2L, "225487", TipoCuenta.CORRIENTE, 100, 700);
        createCuenta(2L, "496825", TipoCuenta.AHORRO, 540, 0);
        createCuenta(3L, "999999", TipoCuenta.AHORRO, 200, 200);

        mockMvc.perform(get("/reportes")
                        .param("fecha", "2022-02-01,2022-02-28")
                        .param("clienteId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(2L))
                .andExpect(jsonPath("$.cliente").value("Marianela Montalvo"))
                .andExpect(jsonPath("$.fechaInicio").value("2022-02-01"))
                .andExpect(jsonPath("$.fechaFin").value("2022-02-28"))
                .andExpect(jsonPath("$.cuentas", hasSize(2)))
                .andExpect(jsonPath("$.cuentas[0].numeroCuenta").value("225487"))
                .andExpect(jsonPath("$.cuentas[0].saldoDisponible").value(700))
                .andExpect(jsonPath("$.cuentas[1].numeroCuenta").value("496825"))
                .andExpect(jsonPath("$.cuentas[1].saldoDisponible").value(0));
    }

    @Test
    void getReportesShouldIncludeMovementsWithinRange() throws Exception {
        Cuenta cuenta = createCuenta(2L, "225487", TipoCuenta.CORRIENTE, 100, 700);
        createMovimiento(cuenta, "2022-02-10T10:30:00", 600, 700);

        mockMvc.perform(get("/reportes")
                        .param("fecha", "2022-02-01,2022-02-28")
                        .param("clienteId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cuentas[0].movimientos", hasSize(1)))
                .andExpect(jsonPath("$.cuentas[0].movimientos[0].fecha").value("2022-02-10"))
                .andExpect(jsonPath("$.cuentas[0].movimientos[0].movimiento").value(600))
                .andExpect(jsonPath("$.cuentas[0].movimientos[0].saldoDisponible").value(700));
    }

    @Test
    void getReportesShouldExcludeMovementsOutsideRange() throws Exception {
        Cuenta cuenta = createCuenta(2L, "225487", TipoCuenta.CORRIENTE, 100, 725);
        createMovimiento(cuenta, "2022-01-31T09:00:00", 100, 200);
        createMovimiento(cuenta, "2022-02-10T10:30:00", 600, 800);
        createMovimiento(cuenta, "2022-03-01T11:00:00", -75, 725);

        mockMvc.perform(get("/reportes")
                        .param("fecha", "2022-02-01,2022-02-28")
                        .param("clienteId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cuentas[0].movimientos", hasSize(1)))
                .andExpect(jsonPath("$.cuentas[0].movimientos[0].fecha").value("2022-02-10"))
                .andExpect(jsonPath("$.cuentas[0].movimientos[0].movimiento").value(600));
    }

    @Test
    void getReportesShouldReturnEmptyMovementsWhenNoMovementsInPeriod() throws Exception {
        Cuenta cuenta = createCuenta(2L, "225487", TipoCuenta.CORRIENTE, 100, 300);
        createMovimiento(cuenta, "2022-01-15T08:00:00", 200, 300);

        mockMvc.perform(get("/reportes")
                        .param("fecha", "2022-02-01,2022-02-28")
                        .param("clienteId", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cuentas", hasSize(1)))
                .andExpect(jsonPath("$.cuentas[0].numeroCuenta").value("225487"))
                .andExpect(jsonPath("$.cuentas[0].movimientos", hasSize(0)));
    }

    @Test
    void getReportesWithInvalidRangeShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/reportes")
                        .param("fecha", "2022-02-01")
                        .param("clienteId", "2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Rango de fechas invalido"));
    }

    @Test
    void getReportesWithClientWithoutAccountsShouldReturnEmptyList() throws Exception {
        clienteSnapshotRepository.save(new ClienteSnapshot(999L, "Cliente Sin Cuentas", "9999999999", true));

        mockMvc.perform(get("/reportes")
                        .param("fecha", "2022-02-01,2022-02-28")
                        .param("clienteId", "999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(999L))
                .andExpect(jsonPath("$.cliente").value("Cliente Sin Cuentas"))
                .andExpect(jsonPath("$.cuentas", hasSize(0)));
    }

    @Test
    void getReportesWithUnknownClientShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/reportes")
                        .param("fecha", "2022-02-01,2022-02-28")
                        .param("clienteId", "998"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Cliente no encontrado"))
                .andExpect(jsonPath("$.path").value("/reportes"));
    }

    private Cuenta createCuenta(
            Long clienteId,
            String numeroCuenta,
            TipoCuenta tipoCuenta,
            int saldoInicial,
            int saldoDisponible
    ) {
        Cuenta cuenta = cuentaRepository.save(new Cuenta(
                null,
                numeroCuenta,
                tipoCuenta,
                BigDecimal.valueOf(saldoInicial),
                true,
                clienteId
        ));
        cuenta.actualizarSaldoDisponible(BigDecimal.valueOf(saldoDisponible));
        return cuentaRepository.save(cuenta);
    }

    private void createMovimiento(
            Cuenta cuenta,
            String fecha,
            int valor,
            int saldo
    ) {
        movimientoRepository.save(new Movimiento(
                null,
                LocalDateTime.parse(fecha),
                BigDecimal.valueOf(valor),
                BigDecimal.valueOf(saldo),
                cuenta
        ));
    }
}
