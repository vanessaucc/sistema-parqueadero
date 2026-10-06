package com.parqueadero.Parqueadero;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class FlujoCompletoYErroresTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String enviarPost(String url, Object body, int estadoEsperado) throws Exception {
        return mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().is(estadoEsperado))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void flujoCompleto_crearTiquete_finalizarConFechaIso_yCobro() throws Exception {
        String conductor = enviarPost("/api/conductores", Map.of(
                "nombre", "Flujo Completo", "identificacion", "FLUJO-001",
                "telefono", "3000000000", "email", "flujo@test.com"), 201);
        String conductorId = JsonPath.read(conductor, "$.id");

        String auto = enviarPost("/api/vehiculos/automoviles", Map.of(
                "placa", "flu001", "marca", "Mazda", "modelo", "3", "tarifaPorHora", 4000.0,
                "conductorId", conductorId, "numeroPuertas", 4, "esElectrico", false), 201);
        String vehiculoId = JsonPath.read(auto, "$.id");

        LocalDateTime ingreso = LocalDateTime.now().minusHours(2);
        String tiquete = enviarPost("/api/tiquetes", Map.of(
                "conductorId", conductorId, "vehiculoId", vehiculoId,
                "fechaIngreso", ingreso.toString()), 201);
        String tiqueteId = JsonPath.read(tiquete, "$.id");

        // Segundo tiquete activo para el mismo vehículo -> 409
        enviarPost("/api/tiquetes", Map.of("conductorId", conductorId, "vehiculoId", vehiculoId), 409);

        // Finaliza usando fechaSalida en formato ISO (sin zona)
        mockMvc.perform(post("/api/tiquetes/" + tiqueteId + "/finalizar")
                        .param("fechaSalida", ingreso.plusHours(2).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is("FINALIZADO")))
                .andExpect(jsonPath("$.cobroFinal", is(8000.0)));

        // Finalizar de nuevo -> 409
        mockMvc.perform(post("/api/tiquetes/" + tiqueteId + "/finalizar"))
                .andExpect(status().isConflict());
    }

    @Test
    void errores_devuelvenElCodigoHttpCorrecto() throws Exception {
        UUID inexistente = UUID.randomUUID();

        // 404: recurso inexistente
        mockMvc.perform(get("/api/conductores/" + inexistente))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
        mockMvc.perform(get("/api/vehiculos/" + inexistente)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/tiquetes/" + inexistente)).andExpect(status().isNotFound());
        mockMvc.perform(get("/api/incidentes/" + inexistente)).andExpect(status().isNotFound());

        // 400: UUID mal formado en la ruta
        mockMvc.perform(get("/api/conductores/no-es-un-uuid")).andExpect(status().isBadRequest());

        // 400: validación de campos
        mockMvc.perform(post("/api/conductores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"\",\"identificacion\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles").isArray());

        // 400: valor de enum inválido
        mockMvc.perform(post("/api/incidentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"tiqueteId\":\"" + inexistente + "\",\"tipo\":\"INVENTADO\",\"descripcion\":\"x\"}"))
                .andExpect(status().isBadRequest());

        // 404: incidente sobre tiquete inexistente
        enviarPost("/api/incidentes", Map.of("tiqueteId", inexistente.toString(), "tipo", "CHOQUE",
                "descripcion", "Choque leve"), 404);
    }

    @Test
    void conductorDuplicado_devuelve409_yPatchIgnoraCamposProtegidos() throws Exception {
        Map<String, Object> body = Map.of("nombre", "Dup Test", "identificacion", "DUP-001");
        String creado = enviarPost("/api/conductores", body, 201);
        enviarPost("/api/conductores", body, 409);

        String id = JsonPath.read(creado, "$.id");

        // Intento de asignación masiva: identificacion y activo deben ser ignorados
        mockMvc.perform(patch("/api/conductores/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Dup Editado\",\"identificacion\":\"HACK\",\"activo\":false}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Dup Editado")))
                .andExpect(jsonPath("$.identificacion", is("DUP-001")))
                .andExpect(jsonPath("$.activo", is(true)));

        // PATCH con nombre en blanco -> 400
        mockMvc.perform(patch("/api/conductores/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"   \"}"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/conductores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].identificacion", hasItem("DUP-001")));
    }

    @Test
    void placaDuplicada_devuelve409() throws Exception {
        String conductor = enviarPost("/api/conductores", Map.of("nombre", "Placa Dup", "identificacion", "PLACA-001"), 201);
        String conductorId = JsonPath.read(conductor, "$.id");

        Map<String, Object> moto = Map.of("placa", "dup99a", "marca", "Honda", "modelo", "CB190",
                "tarifaPorHora", 2000.0, "conductorId", conductorId, "cilindraje", 190,
                "necesitaCasilleroCasco", true);
        enviarPost("/api/vehiculos/motocicletas", moto, 201);
        enviarPost("/api/vehiculos/motocicletas", moto, 409);
    }
}
