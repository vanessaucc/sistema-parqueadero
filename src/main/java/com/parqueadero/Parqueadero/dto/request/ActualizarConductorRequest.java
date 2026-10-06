package com.parqueadero.Parqueadero.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de PATCH: todos los campos son opcionales (null = no modificar).
 * Solo expone nombre, telefono y email; identificacion y activo NO se pueden tocar (anti asignación masiva).
 */
public record ActualizarConductorRequest(
        @Pattern(regexp = ".*\\S.*", message = "El nombre no puede estar en blanco")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
        String telefono,

        @Email(message = "El email no tiene un formato válido")
        @Size(max = 120, message = "El email no puede superar 120 caracteres")
        String email) {
}
