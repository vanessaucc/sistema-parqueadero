package com.parqueadero.Parqueadero.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CrearTiqueteRequest(
        @NotNull(message = "El conductorId es obligatorio")
        UUID conductorId,

        @NotNull(message = "El vehiculoId es obligatorio")
        UUID vehiculoId,

        LocalDateTime fechaIngreso) {
}
