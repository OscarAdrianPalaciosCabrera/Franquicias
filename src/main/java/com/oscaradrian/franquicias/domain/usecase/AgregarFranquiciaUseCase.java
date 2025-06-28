package com.oscaradrian.franquicias.domain.usecase;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import reactor.core.publisher.Mono;

public interface AgregarFranquiciaUseCase {
    Mono<Franquicia> ejecutar(Franquicia franquicia);
}