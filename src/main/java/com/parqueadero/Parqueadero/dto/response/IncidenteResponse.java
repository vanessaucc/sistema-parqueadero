package com.parqueadero.Parqueadero.dto.response;

import com.parqueadero.Parqueadero.domain.TipoIncidente;
import java.time.LocalDateTime;
import java.util.UUID;

public record IncidenteResponse(
        UUID id,
        UUID tiqueteId,
        String placaVehiculo,
        TipoIncidente tipo,
        String descripcion,
        LocalDateTime fechaHora) {
}
