package com.parqueadero.Parqueadero.exception;

/** Se lanza cuando un recurso solicitado (conductor, vehículo, tiquete...) no existe. Se traduce a HTTP 404. */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
