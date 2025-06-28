package com.oscaradrian.franquicias.application.service;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.usecase.ActualizacionNombreFranquiciaUseCase;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ActualizacionNombreFranquiciaService implements ActualizacionNombreFranquiciaUseCase {

    private final FranquiciaRepository repository;

    public ActualizacionNombreFranquiciaService(FranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Franquicia> ejecutar(String nombreActual, String nuevoNombre) {
        if (nuevoNombre == null || nuevoNombre.isBlank()) {
            return Mono.error(new IllegalArgumentException("El nuevo nombre es obligatorio"));
        }

        return repository.findById(nombreActual)
                .switchIfEmpty(Mono.error(new RecursoNoEncontrado("Franquicia no encontrada")))
                .flatMap(franquicia -> {
                    franquicia.setNombre(nuevoNombre);
                    return repository.deleteById(nombreActual)
                            .then(repository.save(franquicia));
                });
    }
}

