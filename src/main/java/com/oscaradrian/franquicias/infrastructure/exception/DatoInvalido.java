package com.oscaradrian.franquicias.infrastructure.exception;

public class DatoInvalido extends RuntimeException {
    public DatoInvalido(String mensaje) {
        super(mensaje);
    }
}
