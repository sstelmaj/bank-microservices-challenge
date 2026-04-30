package com.sofka.bank.clientepersona.infrastructure.web.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
class ClientesCrudTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @Test
    void postClientesShouldRegisterValidClient() throws Exception {
        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validClienteRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").isNumber())
                .andExpect(jsonPath("$.nombre").value("Jose Lema"))
                .andExpect(jsonPath("$.identificacion").value("1234567890"))
                .andExpect(jsonPath("$.estado").value(true))
                .andExpect(jsonPath("$.contrasena").doesNotExist());
    }

    @Test
    void getClientesShouldListClients() throws Exception {
        createCliente(validClienteRequest());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre").value("Jose Lema"))
                .andExpect(jsonPath("$[0].contrasena").doesNotExist());
    }

    @Test
    void getClientesByIdShouldReturnExistingClient() throws Exception {
        Long clienteId = createCliente(validClienteRequest());

        mockMvc.perform(get("/clientes/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(clienteId))
                .andExpect(jsonPath("$.nombre").value("Jose Lema"))
                .andExpect(jsonPath("$.contrasena").doesNotExist());
    }

    @Test
    void putClientesByIdShouldUpdateExistingClient() throws Exception {
        Long clienteId = createCliente(validClienteRequest());

        mockMvc.perform(put("/clientes/{clienteId}", clienteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedClienteRequest()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(clienteId))
                .andExpect(jsonPath("$.nombre").value("Jose Lema Actualizado"))
                .andExpect(jsonPath("$.edad").value(36))
                .andExpect(jsonPath("$.contrasena").doesNotExist());
    }

    @Test
    void deleteClientesByIdShouldDeactivateClient() throws Exception {
        Long clienteId = createCliente(validClienteRequest());

        mockMvc.perform(delete("/clientes/{clienteId}", clienteId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/clientes/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(clienteId))
                .andExpect(jsonPath("$.estado").value(false))
                .andExpect(jsonPath("$.contrasena").doesNotExist());
    }

    @Test
    void responsesShouldNotExposeContrasena() throws Exception {
        Long clienteId = createCliente(validClienteRequest());

        mockMvc.perform(get("/clientes/{clienteId}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contrasena").doesNotExist());

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contrasena").doesNotExist());
    }

    @Test
    void duplicatedIdentificacionShouldReturnConflict() throws Exception {
        createCliente(validClienteRequest());

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validClienteRequest()))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message", containsString("identificacion")))
                .andExpect(jsonPath("$.path").value("/clientes"))
                .andExpect(content().string(containsString("1234567890")));
    }

    @Test
    void nonExistingClienteShouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/clientes/{clienteId}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Cliente no encontrado"))
                .andExpect(jsonPath("$.path").value("/clientes/999"));
    }

    private Long createCliente(String requestBody) throws Exception {
        MvcResult result = mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.get("clienteId").asLong();
    }

    private String validClienteRequest() {
        return """
                {
                  "nombre": "Jose Lema",
                  "genero": "MASCULINO",
                  "edad": 35,
                  "identificacion": "1234567890",
                  "direccion": "Otavalo sn y principal",
                  "telefono": "098254785",
                  "contrasena": "1234",
                  "estado": true
                }
                """;
    }

    private String updatedClienteRequest() {
        return """
                {
                  "nombre": "Jose Lema Actualizado",
                  "genero": "MASCULINO",
                  "edad": 36,
                  "identificacion": "1234567890",
                  "direccion": "Otavalo sn y principal",
                  "telefono": "098254785",
                  "contrasena": "1234",
                  "estado": true
                }
                """;
    }
}
