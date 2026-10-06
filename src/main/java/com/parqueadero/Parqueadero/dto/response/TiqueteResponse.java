package com.parqueadero.Parqueadero.dto.response;

import com.parqueadero.Parqueadero.domain.EstadoTiquete;
import java.time.LocalDateTime;
import java.util.UUID;

public record TiqueteResponse(
        UUID id,
        UUID conductorId,
        String nombreConductor,
        UUID vehiculoId,
        String placaVehiculo,
        String tipoVehiculo,
        LocalDateTime fechaIngreso,
        LocalDateTime fechaSalida,
        EstadoTiquete estado,
        double cobroFinal) {
}
