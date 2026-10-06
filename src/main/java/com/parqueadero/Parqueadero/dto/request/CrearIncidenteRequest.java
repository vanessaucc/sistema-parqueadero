package com.parqueadero.Parqueadero.dto.request;

import com.parqueadero.Parqueadero.domain.TipoIncidente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record CrearIncidenteRequest(
        @NotNull(message = "El tiqueteId es obligatorio")
        UUID tiqueteId,

        @NotNull(message = "El tipo de incidente es obligatorio")
        TipoIncidente tipo,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 1000, message = "La descripción no puede superar 1000 caracteres")
        String descripcion,

        LocalDateTime fechaHora) {
}
