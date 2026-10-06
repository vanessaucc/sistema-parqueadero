package com.parqueadero.Parqueadero.dto.response;

import com.parqueadero.Parqueadero.domain.EstadoTiquete;
import java.time.LocalDateTime;
import java.util.UUID;

public record TiqueteItemResponse(
        UUID idTiquete,
        String placaVehiculo,
        String tipoVehiculo,
        LocalDateTime fechaIngreso,
        LocalDateTime fechaSalida,
        EstadoTiquete estado,
        Double cobroFinal) {
}
