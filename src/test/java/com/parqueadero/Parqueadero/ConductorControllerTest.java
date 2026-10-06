package com.parqueadero.Parqueadero;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parqueadero.Parqueadero.domain.*;
import com.parqueadero.Parqueadero.dto.request.ActualizarConductorRequest;
import com.parqueadero.Parqueadero.dto.request.CrearConductorRequest;
import com.parqueadero.Parqueadero.repository.ConductorRepository;
import com.parqueadero.Parqueadero.repository.TiqueteRepository;
import com.parqueadero.Parqueadero.repository.VehiculoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class ConductorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConductorRepository conductorRepository;

    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Autowired
    private TiqueteRepository tiqueteRepository;

    @Test
    void testCrearConductorResumenYPatch() throws Exception {
        // 1. Crear Conductor via POST
        CrearConductorRequest crearDto = new CrearConductorRequest(
                "Maria Gonzalez",
                "1234567890",
                "3112223344",
                "maria@parqueadero.com"
        );

        String jsonResponse = mockMvc.perform(post("/api/conductores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(crearDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre", is("Maria Gonzalez")))
                .andExpect(jsonPath("$.identificacion", is("1234567890")))
                .andReturn().getResponse().getContentAsString();

        Conductor conductor = conductorRepository.findByIdentificacion("1234567890").orElseThrow();

        // 2. Asociar un vehículo y tiquete finalizado para probar el resumen
        Automovil auto = new Automovil("MGZ100", "Renault", "Clio", 4000.0, conductor, 5, false);
        vehiculoRepository.save(auto);

        Tiquete tiquete = new Tiquete(conductor, auto, LocalDateTime.now().minusHours(3));
        tiquete.finalizar(LocalDateTime.now());
        tiqueteRepository.save(tiquete);

        // 3. Consultar Resumen Ejecutivo
        mockMvc.perform(get("/api/conductores/" + conductor.getId() + "/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Maria Gonzalez")))
                .andExpect(jsonPath("$.totalTiquetesRealizados", is(1)))
                .andExpect(jsonPath("$.montoTotalGastado", is(12000.0)))
                .andExpect(jsonPath("$.historialTiquetes[0].placaVehiculo", is("MGZ100")));

        // 4. Actualización Parcial Segura (PATCH)
        ActualizarConductorRequest patchDto = new ActualizarConductorRequest("Maria G. Torres", null, "maria_nueva@email.com");

        mockMvc.perform(patch("/api/conductores/" + conductor.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Maria G. Torres")))
                .andExpect(jsonPath("$.email", is("maria_nueva@email.com")))
                .andExpect(jsonPath("$.identificacion", is("1234567890"))); // Identificación se mantiene intacta
    }
}
