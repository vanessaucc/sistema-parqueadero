package com.parqueadero.Parqueadero.dto.response;

import java.util.List;
import java.util.UUID;

public record ConductorResumenResponse(
        UUID id,
        String nombre,
        String identificacion,
        String telefono,
        String email,
        boolean activo,
        int totalTiquetesRealizados,
        double montoTotalGastado,
        List<TiqueteItemResponse> historialTiquetes) {
}
