package com.parqueadero.Parqueadero;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parqueadero.Parqueadero.domain.*;
import com.parqueadero.Parqueadero.dto.request.CrearIncidenteRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class DisponibilidadYIncidentesControllerTest {

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
    void testDisponibilidadEnTiempoRealYRegistroDeIncidente() throws Exception {
        Conductor conductor = conductorRepository.save(
                new Conductor("Pedro Gomez", "9876543210", "3159998877", "pedro@test.com")
        );

        Automovil auto = new Automovil("TST123", "Chevrolet", "Onix", 3500.0, conductor, 5, false);
        vehiculoRepository.save(auto);

        Motocicleta moto = new Motocicleta("TST456", "Bajaj", "Pulsar", 2000.0, conductor, 200, true);
        vehiculoRepository.save(moto);

        // Crear 1 tiquete activo para auto y 1 para moto
        Tiquete tiqueteAuto = new Tiquete(conductor, auto, LocalDateTime.now());
        tiqueteRepository.save(tiqueteAuto);

        Tiquete tiqueteMoto = new Tiquete(conductor, moto, LocalDateTime.now());
        tiqueteRepository.save(tiqueteMoto);

        // 1. Probar Disponibilidad en tiempo real
        // En application-test.properties: capacidad automoviles=20, motocicletas=10 -> total = 30
        mockMvc.perform(get("/api/parqueadero/disponibilidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.capacidadTotal", is(30)))
                .andExpect(jsonPath("$.celdasOcupadas", is(2)))
                .andExpect(jsonPath("$.espaciosLibres", is(28)))
                .andExpect(jsonPath("$.ocupadasAutomoviles", is(1)))
                .andExpect(jsonPath("$.disponiblesAutomoviles", is(19)))
                .andExpect(jsonPath("$.ocupadasMotocicletas", is(1)))
                .andExpect(jsonPath("$.disponiblesMotocicletas", is(9)));

        // 2. Registrar Incidente sobre el tiquete del automóvil
        CrearIncidenteRequest incidenteReq = new CrearIncidenteRequest(
                tiqueteAuto.getId(),
                TipoIncidente.RAYON,
                "Rayón superficial en la puerta del conductor mientras salía otro vehículo",
                LocalDateTime.now()
        );

        mockMvc.perform(post("/api/incidentes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incidenteReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo", is("RAYON")))
                .andExpect(jsonPath("$.placaVehiculo", is("TST123")))
                .andExpect(jsonPath("$.tiqueteId", is(tiqueteAuto.getId().toString())));
    }
}
