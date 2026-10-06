package com.parqueadero.Parqueadero.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CrearMotocicletaRequest(
        @NotBlank(message = "La placa es obligatoria")
        @Size(max = 10, message = "La placa no puede superar 10 caracteres")
        String placa,

        @NotBlank(message = "La marca es obligatoria")
        @Size(max = 50, message = "La marca no puede superar 50 caracteres")
        String marca,

        @NotBlank(message = "El modelo es obligatorio")
        @Size(max = 50, message = "El modelo no puede superar 50 caracteres")
        String modelo,

        @NotNull(message = "La tarifa por hora es obligatoria")
        @PositiveOrZero(message = "La tarifa por hora no puede ser negativa")
        Double tarifaPorHora,

        @NotNull(message = "El conductorId es obligatorio")
        UUID conductorId,

        @NotNull(message = "El cilindraje es obligatorio")
        @Min(value = 1, message = "El cilindraje debe ser mayor a 0")
        Integer cilindraje,

        boolean necesitaCasilleroCasco) {
}
