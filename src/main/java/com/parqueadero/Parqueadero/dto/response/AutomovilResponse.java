package com.parqueadero.Parqueadero.dto.response;

public class AutomovilResponse extends VehiculoResponse {
    private int numeroPuertas;
    private boolean esElectrico;

    public int getNumeroPuertas() {
        return numeroPuertas;
    }

    public void setNumeroPuertas(int numeroPuertas) {
        this.numeroPuertas = numeroPuertas;
    }

    public boolean isEsElectrico() {
        return esElectrico;
    }

    public void setEsElectrico(boolean esElectrico) {
        this.esElectrico = esElectrico;
    }
}
