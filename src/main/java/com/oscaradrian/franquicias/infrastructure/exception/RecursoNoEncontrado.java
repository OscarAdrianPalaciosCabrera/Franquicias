package com.oscaradrian.franquicias.infrastructure.exception;

public class RecursoNoEncontrado extends RuntimeException {
    public RecursoNoEncontrado(String mensaje) {
        super(mensaje);
    }
}