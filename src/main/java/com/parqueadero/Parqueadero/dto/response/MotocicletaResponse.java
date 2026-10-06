package com.parqueadero.Parqueadero.dto.response;

public class MotocicletaResponse extends VehiculoResponse {
    private int cilindraje;
    private boolean necesitaCasilleroCasco;

    public int getCilindraje() {
        return cilindraje;
    }

    public void setCilindraje(int cilindraje) {
        this.cilindraje = cilindraje;
    }

    public boolean isNecesitaCasilleroCasco() {
        return necesitaCasilleroCasco;
    }

    public void setNecesitaCasilleroCasco(boolean necesitaCasilleroCasco) {
        this.necesitaCasilleroCasco = necesitaCasilleroCasco;
    }
}
