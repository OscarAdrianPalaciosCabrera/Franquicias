package com.oscaradrian.franquicias.domain.usecase;

import reactor.core.publisher.Mono;

public interface ActualizacionNombreProductoUseCase {
    Mono<Void> ejecutar(String nombreFranquicia, String nombreSucursal, String nombreProductoActual, String nuevoNombre);
}
