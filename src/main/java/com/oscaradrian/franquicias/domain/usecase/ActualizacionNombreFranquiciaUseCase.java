package com.oscaradrian.franquicias.domain.usecase;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import reactor.core.publisher.Mono;

public interface ActualizacionNombreFranquiciaUseCase {
    Mono<Franquicia> ejecutar(String nombreActual, String nuevoNombre);
}
