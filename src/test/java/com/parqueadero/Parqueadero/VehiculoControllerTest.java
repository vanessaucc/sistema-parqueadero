package com.parqueadero.Parqueadero;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parqueadero.Parqueadero.domain.Conductor;
import com.parqueadero.Parqueadero.dto.request.CrearAutomovilRequest;
import com.parqueadero.Parqueadero.dto.request.CrearMotocicletaRequest;
import com.parqueadero.Parqueadero.repository.ConductorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class VehiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConductorRepository conductorRepository;

    private Conductor conductor;

    @BeforeEach
    void setUp() {
        conductor = conductorRepository.save(
                new Conductor("Juan Perez", "1098765432", "3001234567", "juan@test.com")
        );
    }

    @Test
    void testCrearAutomovilYMotocicletaYListarPolimorfico() throws Exception {
        // 1. Crear Automóvil
        CrearAutomovilRequest autoReq = new CrearAutomovilRequest(
                "ABC123",
                "Toyota",
                "Corolla 2023",
                5000.0,
                conductor.getId(),
                4,
                true
        );

        mockMvc.perform(post("/api/vehiculos/automoviles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(autoReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placa", is("ABC123")))
                .andExpect(jsonPath("$.tipo", is("AUTOMOVIL")))
                .andExpect(jsonPath("$.numeroPuertas", is(4)))
                .andExpect(jsonPath("$.esElectrico", is(true)));

        // 2. Crear Motocicleta
        CrearMotocicletaRequest motoReq = new CrearMotocicletaRequest(
                "XYZ789",
                "Yamaha",
                "MT-03",
                2500.0,
                conductor.getId(),
                321,
                true
        );

        mockMvc.perform(post("/api/vehiculos/motocicletas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(motoReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.placa", is("XYZ789")))
                .andExpect(jsonPath("$.tipo", is("MOTOCICLETA")))
                .andExpect(jsonPath("$.cilindraje", is(321)))
                .andExpect(jsonPath("$.necesitaCasilleroCasco", is(true)));

        // 3. Listar Polimórfico
        mockMvc.perform(get("/api/vehiculos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].tipo", hasItems("AUTOMOVIL", "MOTOCICLETA")));
    }
}
