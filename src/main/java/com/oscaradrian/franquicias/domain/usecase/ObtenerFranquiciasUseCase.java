package com.oscaradrian.franquicias.domain.usecase;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import reactor.core.publisher.Flux;

public interface ObtenerFranquiciasUseCase {
    Flux<Franquicia> ejecutar();
}
