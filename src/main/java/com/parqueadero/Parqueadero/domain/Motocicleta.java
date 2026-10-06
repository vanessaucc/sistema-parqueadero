package com.parqueadero.Parqueadero.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "motocicletas")
public class Motocicleta extends Vehiculo {

    @Column(nullable = false)
    private int cilindraje;

    @Column(name = "necesita_casillero_casco", nullable = false)
    private boolean necesitaCasilleroCasco;

    protected Motocicleta() {
    }

    public Motocicleta(String placa, String marca, String modelo, double tarifaPorHora, Conductor conductor,
                       int cilindraje, boolean necesitaCasilleroCasco) {
        super(placa, marca, modelo, tarifaPorHora, conductor);
        if (cilindraje < 1) {
            throw new IllegalArgumentException("El cilindraje debe ser mayor a 0");
        }
        this.cilindraje = cilindraje;
        this.necesitaCasilleroCasco = necesitaCasilleroCasco;
    }

    public int getCilindraje() {
        return cilindraje;
    }

    public boolean isNecesitaCasilleroCasco() {
        return necesitaCasilleroCasco;
    }

    @Override
    public String getTipoVehiculo() {
        return "MOTOCICLETA";
    }
}
