package com.oscaradrian.franquicias.infrastructure.exception;

public class RecursoYaExiste extends RuntimeException {
    public RecursoYaExiste(String mensaje) {
        super(mensaje);
    }
}