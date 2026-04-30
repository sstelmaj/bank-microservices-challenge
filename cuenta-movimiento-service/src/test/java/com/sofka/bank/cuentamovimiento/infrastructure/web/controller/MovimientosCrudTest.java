package com.sofka.bank.cuentamovimiento.infrastructure.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sofka.bank.cuentamovimiento.domain.model.ClienteSnapshot;
import com.sofka.bank.cuentamovimiento.domain.repository.ClienteSnapshotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@TestPropertySource(properties = {
        "spring.jpa.open-in-view=false"
})
class MovimientosCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteSnapshotRepository clienteSnapshotRepository;

    @BeforeEach
    void setUpSnapshots() {
        clienteSnapshotRepository.save(new ClienteSnapshot(1L, "Jose Lema", "1234567890", true));
    }

    @Test
    void postMovimientosWithPositiveValueShouldRegisterDepositoAndIncreaseSaldo() throws Exception {
        Long cuentaId = createCuenta(validCuentaRequest("478758", 1000));

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("478758", 600)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tipoMovimiento").value("DEPOSITO"))
                .andExpect(jsonPath("$.valor").value(600))
                .andExpect(jsonPath("$.saldo").value(1600))
                .andExpect(jsonPath("$.numeroCuenta").value("478758"))
                .andExpect(jsonPath("$.fecha").isNotEmpty());

        mockMvc.perform(get("/cuentas/{id}", cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoDisponible").value(1600));
    }

    @Test
    void postMovimientosWithNegativeValueShouldRegisterRetiroAndDecreaseSaldo() throws Exception {
        Long cuentaId = createCuenta(validCuentaRequest("478759", 1000));

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("478759", -400)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoMovimiento").value("RETIRO"))
                .andExpect(jsonPath("$.valor").value(-400))
                .andExpect(jsonPath("$.saldo").value(600))
                .andExpect(jsonPath("$.numeroCuenta").value("478759"));

        mockMvc.perform(get("/cuentas/{id}", cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoDisponible").value(600));
    }

    @Test
    void postMovimientosWithExactRetiroShouldLeaveSaldoInZero() throws Exception {
        Long cuentaId = createCuenta(validCuentaRequest("478760", 1000));

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("478760", -1000)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoMovimiento").value("RETIRO"))
                .andExpect(jsonPath("$.saldo").value(0));

        mockMvc.perform(get("/cuentas/{id}", cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoDisponible").value(0));
    }

    @Test
    void postMovimientosWithRetiroGreaterThanSaldoShouldReturnSaldoNoDisponible() throws Exception {
        createCuenta(validCuentaRequest("478761", 1000));

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("478761", -1001)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));
    }

    @Test
    void rejectedRetiroShouldNotAlterSaldo() throws Exception {
        Long cuentaId = createCuenta(validCuentaRequest("478762", 1000));

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("478762", -1001)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));

        mockMvc.perform(get("/cuentas/{id}", cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoDisponible").value(1000));
    }

    @Test
    void rejectedRetiroShouldNotBeRegistered() throws Exception {
        createCuenta(validCuentaRequest("478763", 1000));

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("478763", -1001)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Saldo no disponible"));

        mockMvc.perform(get("/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void postMovimientosWithZeroValueShouldReturnBadRequest() throws Exception {
        createCuenta(validCuentaRequest("478764", 1000));

        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("478764", 0)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("no puede ser cero")));
    }

    @Test
    void postMovimientosWithNonExistingCuentaShouldReturnNotFound() throws Exception {
        mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("999999", 100)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cuenta no encontrada"));
    }

    @Test
    void getMovimientosShouldListSuccessfulMovimientos() throws Exception {
        createCuenta(validCuentaRequest("478765", 1000));
        createCuenta(validCuentaRequest("478766", 500));

        createMovimiento("478765", 200);
        createMovimiento("478766", -100);

        mockMvc.perform(get("/movimientos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numeroCuenta").value("478765"))
                .andExpect(jsonPath("$[0].tipoMovimiento").value("DEPOSITO"))
                .andExpect(jsonPath("$[1].numeroCuenta").value("478766"))
                .andExpect(jsonPath("$[1].tipoMovimiento").value("RETIRO"));
    }

    @Test
    void getMovimientosByIdShouldReturnExistingMovimiento() throws Exception {
        createCuenta(validCuentaRequest("478767", 1000));
        Long movimientoId = createMovimiento("478767", 300);

        mockMvc.perform(get("/movimientos/{id}", movimientoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(movimientoId))
                .andExpect(jsonPath("$.numeroCuenta").value("478767"))
                .andExpect(jsonPath("$.tipoMovimiento").value("DEPOSITO"))
                .andExpect(jsonPath("$.saldo").value(1300));
    }

    @Test
    void getMovimientosByIdWithUnknownIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/movimientos/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movimiento no encontrado"));
    }

    @Test
    void putMovimientosByIdShouldRejectAppliedMovementWithoutChangingSaldo() throws Exception {
        Long cuentaId = createCuenta(validCuentaRequest("478768", 1000));
        Long movimientoId = createMovimiento("478768", 300);

        mockMvc.perform(put("/movimientos/{id}", movimientoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("478768", 999)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Los movimientos aplicados no pueden modificarse por integridad transaccional"));

        mockMvc.perform(get("/movimientos/{id}", movimientoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(300))
                .andExpect(jsonPath("$.saldo").value(1300));

        mockMvc.perform(get("/cuentas/{id}", cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoDisponible").value(1300));
    }

    @Test
    void deleteMovimientosByIdShouldRejectAppliedMovementWithoutRemovingIt() throws Exception {
        Long cuentaId = createCuenta(validCuentaRequest("478769", 1000));
        Long movimientoId = createMovimiento("478769", -200);

        mockMvc.perform(delete("/movimientos/{id}", movimientoId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Los movimientos aplicados no pueden eliminarse por integridad transaccional"));

        mockMvc.perform(get("/movimientos/{id}", movimientoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor").value(-200))
                .andExpect(jsonPath("$.saldo").value(800));

        mockMvc.perform(get("/cuentas/{id}", cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldoDisponible").value(800));
    }

    @Test
    void putMovimientosByIdWithUnknownIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(put("/movimientos/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest("478768", 100)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movimiento no encontrado"));
    }

    @Test
    void deleteMovimientosByIdWithUnknownIdShouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/movimientos/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Movimiento no encontrado"));
    }

    private Long createCuenta(String requestBody) throws Exception {
        MvcResult result = mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.get("id").asLong();
    }

    private Long createMovimiento(String numeroCuenta, int valor) throws Exception {
        MvcResult result = mockMvc.perform(post("/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validMovimientoRequest(numeroCuenta, valor)))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.get("id").asLong();
    }

    private String validCuentaRequest(String numeroCuenta, int saldoInicial) {
        return """
                {
                  "numeroCuenta": "%s",
                  "tipoCuenta": "AHORRO",
                  "saldoInicial": %s,
                  "estado": true,
                  "clienteId": 1
                }
                """.formatted(numeroCuenta, saldoInicial);
    }

    private String validMovimientoRequest(String numeroCuenta, int valor) {
        return """
                {
                  "numeroCuenta": "%s",
                  "valor": %s
                }
                """.formatted(numeroCuenta, valor);
    }
}
