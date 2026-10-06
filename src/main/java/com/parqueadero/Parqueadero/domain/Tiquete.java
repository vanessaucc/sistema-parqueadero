package com.parqueadero.Parqueadero.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tiquetes")
public class Tiquete {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conductor_id", nullable = false)
    private Conductor conductor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @Embedded
    private RangoEstancia periodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoTiquete estado;

    @Column(name = "cobro_final", nullable = false)
    private double cobroFinal;

    @OneToMany(mappedBy = "tiquete", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Incidente> incidentes = new ArrayList<>();

    protected Tiquete() {
    }

    public Tiquete(Conductor conductor, Vehiculo vehiculo, LocalDateTime fechaIngreso) {
        if (conductor == null)
            throw new IllegalArgumentException("El conductor es obligatorio");
        if (vehiculo == null)
            throw new IllegalArgumentException("El vehículo es obligatorio");

        LocalDateTime ingreso = fechaIngreso != null ? fechaIngreso : LocalDateTime.now();
        this.id = UUID.randomUUID();
        this.conductor = conductor;
        this.vehiculo = vehiculo;
        this.periodo = new RangoEstancia(ingreso);
        this.estado = EstadoTiquete.ACTIVO;
        this.cobroFinal = 0.0;
    }

    public void finalizar(LocalDateTime fechaSalida) {
        if (this.estado != EstadoTiquete.ACTIVO) {
            throw new IllegalStateException("El tiquete ya ha sido finalizado");
        }
        LocalDateTime salida = fechaSalida != null ? fechaSalida : LocalDateTime.now();
        this.periodo = this.periodo.finalizar(salida);
        this.estado = EstadoTiquete.FINALIZADO;
        this.cobroFinal = this.vehiculo.getTarifaPorHora() * this.periodo.getDuracionHoras();
    }

    public UUID getId() {
        return id;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public RangoEstancia getPeriodo() {
        return periodo;
    }

    public EstadoTiquete getEstado() {
        return estado;
    }

    public double getCobroFinal() {
        return cobroFinal;
    }

    public List<Incidente> getIncidentes() {
        return Collections.unmodifiableList(incidentes);
    }
}
