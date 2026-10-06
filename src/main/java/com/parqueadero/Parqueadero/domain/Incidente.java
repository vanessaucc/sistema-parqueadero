package com.parqueadero.Parqueadero.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "incidentes")
public class Incidente {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tiquete_id", nullable = false)
    private Tiquete tiquete;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoIncidente tipo;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    protected Incidente() {
    }

    public Incidente(Tiquete tiquete, TipoIncidente tipo, String descripcion, LocalDateTime fechaHora) {
        if (tiquete == null)
            throw new IllegalArgumentException("El tiquete es obligatorio");
        if (tipo == null)
            throw new IllegalArgumentException("El tipo de incidente es obligatorio");
        if (descripcion == null || descripcion.isBlank())
            throw new IllegalArgumentException("La descripción es obligatoria");

        this.id = UUID.randomUUID();
        this.tiquete = tiquete;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora != null ? fechaHora : LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public Tiquete getTiquete() {
        return tiquete;
    }

    public TipoIncidente getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }
}
