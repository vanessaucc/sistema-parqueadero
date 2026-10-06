package com.parqueadero.Parqueadero.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Embeddable
public record RangoEstancia(
        @Column(name = "fecha_ingreso", nullable = false)
        LocalDateTime fechaIngreso,

        @Column(name = "fecha_salida")
        LocalDateTime fechaSalida
) {
    public RangoEstancia {
        if (fechaIngreso == null) {
            throw new IllegalArgumentException("La fecha de ingreso es obligatoria");
        }
        if (fechaSalida != null && fechaSalida.isBefore(fechaIngreso)) {
            throw new IllegalArgumentException("La fecha de salida no puede ser anterior a la fecha de ingreso");
        }
    }

    public RangoEstancia(LocalDateTime fechaIngreso) {
        this(fechaIngreso, null);
    }

    public RangoEstancia finalizar(LocalDateTime salida) {
        return new RangoEstancia(this.fechaIngreso, salida != null ? salida : LocalDateTime.now());
    }

    public long getDuracionMinutos() {
        LocalDateTime fin = fechaSalida != null ? fechaSalida : LocalDateTime.now();
        return ChronoUnit.MINUTES.between(fechaIngreso, fin);
    }

    public long getDuracionHoras() {
        long minutos = getDuracionMinutos();
        return Math.max(1, (long) Math.ceil((double) minutos / 60.0));
    }
}
