package com.parqueadero.Parqueadero.domain;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "vehiculos")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Vehiculo {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true, length = 10)
    private String placa;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(name = "tarifa_por_hora", nullable = false)
    private double tarifaPorHora;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conductor_id", nullable = false)
    private Conductor conductor;

    protected Vehiculo() {
    }

    public Vehiculo(String placa, String marca, String modelo, double tarifaPorHora, Conductor conductor) {
        if (placa == null || placa.isBlank())
            throw new IllegalArgumentException("La placa es obligatoria");
        if (marca == null || marca.isBlank())
            throw new IllegalArgumentException("La marca es obligatoria");
        if (modelo == null || modelo.isBlank())
            throw new IllegalArgumentException("El modelo es obligatorio");
        if (tarifaPorHora < 0)
            throw new IllegalArgumentException("La tarifa por hora no puede ser negativa");
        if (conductor == null)
            throw new IllegalArgumentException("El conductor es obligatorio");

        this.id = UUID.randomUUID();
        this.placa = placa.trim().toUpperCase();
        this.marca = marca;
        this.modelo = modelo;
        this.tarifaPorHora = tarifaPorHora;
        this.conductor = conductor;
    }

    public UUID getId() {
        return id;
    }

    public String getPlaca() {
        return placa;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getTarifaPorHora() {
        return tarifaPorHora;
    }

    public Conductor getConductor() {
        return conductor;
    }

    public abstract String getTipoVehiculo();
}
