package com.oscaradrian.franquicias.application.service;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.usecase.AgregarFranquiciaUseCase;
import com.oscaradrian.franquicias.domain.usecase.ObtenerFranquiciasUseCase;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoYaExiste;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.springframework.stereotype.Service;

@Service
public class FranquiciaService implements AgregarFranquiciaUseCase, ObtenerFranquiciasUseCase {

    private final FranquiciaRepository repository;

    public FranquiciaService(FranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Franquicia> ejecutar(Franquicia franquicia) {
        if (franquicia.getNombre() == null || franquicia.getNombre().isBlank()) {
            return Mono.error(new IllegalArgumentException("El nombre de la franquicia es obligatorio"));
        }

        return repository.findById(franquicia.getNombre())
                .flatMap(existing -> Mono.<Franquicia>error(new RecursoYaExiste("Ya existe una franquicia con ese nombre")))
                .switchIfEmpty(Mono.defer(() -> repository.save(franquicia)));


    }

    @Override
    public Flux<Franquicia> ejecutar() {
        return repository.findAll();
    }
}