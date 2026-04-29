package com.sofka.bank.cuentamovimiento.infrastructure.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class CuentasCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void postCuentasShouldCreateValidCuenta() throws Exception {
        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCuentaRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.numeroCuenta").value("478758"))
                .andExpect(jsonPath("$.tipoCuenta").value("AHORRO"))
                .andExpect(jsonPath("$.saldoInicial").value(2000))
                .andExpect(jsonPath("$.saldoDisponible").value(2000))
                .andExpect(jsonPath("$.estado").value(true))
                .andExpect(jsonPath("$.clienteId").value(1L));
    }

    @Test
    void getCuentasShouldListCuentas() throws Exception {
        createCuenta(validCuentaRequest());

        mockMvc.perform(get("/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].numeroCuenta").value("478758"))
                .andExpect(jsonPath("$[0].saldoDisponible").value(2000));
    }

    @Test
    void getCuentasByIdShouldReturnExistingCuenta() throws Exception {
        Long cuentaId = createCuenta(validCuentaRequest());

        mockMvc.perform(get("/cuentas/{id}", cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cuentaId))
                .andExpect(jsonPath("$.numeroCuenta").value("478758"))
                .andExpect(jsonPath("$.clienteId").value(1L));
    }

    @Test
    void putCuentasByIdShouldUpdateAllowedFields() throws Exception {
        Long cuentaId = createCuenta(validCuentaRequest());

        mockMvc.perform(put("/cuentas/{id}", cuentaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCuentaRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cuentaId))
                .andExpect(jsonPath("$.numeroCuenta").value("478759"))
                .andExpect(jsonPath("$.tipoCuenta").value("CORRIENTE"))
                .andExpect(jsonPath("$.saldoInicial").value(2000))
                .andExpect(jsonPath("$.saldoDisponible").value(2000))
                .andExpect(jsonPath("$.estado").value(true))
                .andExpect(jsonPath("$.clienteId").value(2L));
    }

    @Test
    void deleteCuentasByIdShouldDeactivateCuenta() throws Exception {
        Long cuentaId = createCuenta(validCuentaRequest());

        mockMvc.perform(delete("/cuentas/{id}", cuentaId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/cuentas/{id}", cuentaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(cuentaId))
                .andExpect(jsonPath("$.estado").value(false));
    }

    @Test
    void duplicatedNumeroCuentaShouldReturnConflict() throws Exception {
        createCuenta(validCuentaRequest());

        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCuentaRequest()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message", containsString("numero")))
                .andExpect(content().string(containsString("478758")));
    }

    @Test
    void nonExistingCuentaShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/cuentas/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cuenta no encontrada"));
    }

    @Test
    void negativeSaldoInicialShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuentaWithNegativeSaldoInicialRequest()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("saldo inicial")));
    }

    @Test
    void missingClienteIdShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuentaWithoutClienteIdRequest()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("clienteId")));
    }

    @Test
    void unknownClienteInSnapshotShouldReturnNotFound() throws Exception {
        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCuentaRequest()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Cliente no encontrado"));
    }

    @Test
    void inactiveClienteInSnapshotShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCuentaRequest()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cliente inactivo"));
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

    private String validCuentaRequest() {
        return """
                {
                  "numeroCuenta": "478758",
                  "tipoCuenta": "AHORRO",
                  "saldoInicial": 2000,
                  "estado": true,
                  "clienteId": 1
                }
                """;
    }

    private String updatedCuentaRequest() {
        return """
                {
                  "numeroCuenta": "478759",
                  "tipoCuenta": "CORRIENTE",
                  "saldoInicial": 2000,
                  "estado": true,
                  "clienteId": 2
                }
                """;
    }

    private String cuentaWithNegativeSaldoInicialRequest() {
        return """
                {
                  "numeroCuenta": "478760",
                  "tipoCuenta": "AHORRO",
                  "saldoInicial": -1,
                  "estado": true,
                  "clienteId": 1
                }
                """;
    }

    private String cuentaWithoutClienteIdRequest() {
        return """
                {
                  "numeroCuenta": "478761",
                  "tipoCuenta": "AHORRO",
                  "saldoInicial": 100,
                  "estado": true
                }
                """;
    }
}
