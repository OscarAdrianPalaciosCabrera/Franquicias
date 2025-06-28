package com.oscaradrian.franquicias.domain.usecase;

import reactor.core.publisher.Mono;

public interface ActualizacionNombreSucursalUseCase {
    Mono<Void> ejecutar(String nombreFranquicia, String nombreSucursalActual, String nuevoNombre);
}
