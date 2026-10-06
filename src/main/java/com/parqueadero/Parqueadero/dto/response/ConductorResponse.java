package com.parqueadero.Parqueadero.dto.response;

import java.util.UUID;

public record ConductorResponse(
        UUID id,
        String nombre,
        String identificacion,
        String telefono,
        String email,
        boolean activo) {
}
