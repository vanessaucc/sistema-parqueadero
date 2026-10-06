package com.parqueadero.Parqueadero.exception;

/** Se lanza cuando la operación choca con el estado actual (duplicados, cupo lleno...). Se traduce a HTTP 409. */
public class ConflictoException extends RuntimeException {

    public ConflictoException(String mensaje) {
        super(mensaje);
    }
}
