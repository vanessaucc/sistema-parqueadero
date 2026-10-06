package com.parqueadero.Parqueadero.service;

import com.parqueadero.Parqueadero.domain.EstadoTiquete;
import com.parqueadero.Parqueadero.domain.Vehiculo;
import com.parqueadero.Parqueadero.dto.response.DisponibilidadResponse;
import com.parqueadero.Parqueadero.repository.TiqueteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DisponibilidadService {

    private final TiqueteRepository tiqueteRepository;
    private final int capacidadAutomoviles;
    private final int capacidadMotocicletas;

    public DisponibilidadService(
            TiqueteRepository tiqueteRepository,
            @Value("${parqueadero.capacidad.automoviles:50}") int capacidadAutomoviles,
            @Value("${parqueadero.capacidad.motocicletas:30}") int capacidadMotocicletas) {
        this.tiqueteRepository = tiqueteRepository;
        this.capacidadAutomoviles = capacidadAutomoviles;
        this.capacidadMotocicletas = capacidadMotocicletas;
    }

    @Transactional(readOnly = true)
    public DisponibilidadResponse consultarDisponibilidad() {
        int ocupadasAutomoviles = (int) tiqueteRepository.countOcupadasAutomoviles(EstadoTiquete.ACTIVO);
        int ocupadasMotocicletas = (int) tiqueteRepository.countOcupadasMotocicletas(EstadoTiquete.ACTIVO);

        int disponiblesAutomoviles = Math.max(0, capacidadAutomoviles - ocupadasAutomoviles);
        int disponiblesMotocicletas = Math.max(0, capacidadMotocicletas - ocupadasMotocicletas);

        int capacidadTotal = capacidadAutomoviles + capacidadMotocicletas;
        int celdasOcupadas = ocupadasAutomoviles + ocupadasMotocicletas;
        int espaciosLibres = Math.max(0, capacidadTotal - celdasOcupadas);

        return new DisponibilidadResponse(
                capacidadTotal,
                celdasOcupadas,
                espaciosLibres,
                capacidadAutomoviles,
                ocupadasAutomoviles,
                disponiblesAutomoviles,
                capacidadMotocicletas,
                ocupadasMotocicletas,
                disponiblesMotocicletas
        );
    }

    /** Indica si hay al menos una celda libre para el tipo del vehículo indicado. */
    @Transactional(readOnly = true)
    public boolean hayEspacioPara(Vehiculo vehiculo) {
        DisponibilidadResponse d = consultarDisponibilidad();
        return switch (vehiculo.getTipoVehiculo()) {
            case "AUTOMOVIL" -> d.disponiblesAutomoviles() > 0;
            case "MOTOCICLETA" -> d.disponiblesMotocicletas() > 0;
            default -> false;
        };
    }
}
