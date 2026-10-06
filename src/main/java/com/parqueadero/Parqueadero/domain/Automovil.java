package com.parqueadero.Parqueadero.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "automoviles")
public class Automovil extends Vehiculo {

    @Column(name = "numero_puertas", nullable = false)
    private int numeroPuertas;

    @Column(name = "es_electrico", nullable = false)
    private boolean esElectrico;

    protected Automovil() {
    }

    public Automovil(String placa, String marca, String modelo, double tarifaPorHora, Conductor conductor,
                     int numeroPuertas, boolean esElectrico) {
        super(placa, marca, modelo, tarifaPorHora, conductor);
        if (numeroPuertas < 1) {
            throw new IllegalArgumentException("El número de puertas debe ser al menos 1");
        }
        this.numeroPuertas = numeroPuertas;
        this.esElectrico = esElectrico;
    }

    public int getNumeroPuertas() {
        return numeroPuertas;
    }

    public boolean isEsElectrico() {
        return esElectrico;
    }

    @Override
    public String getTipoVehiculo() {
        return "AUTOMOVIL";
    }
}
