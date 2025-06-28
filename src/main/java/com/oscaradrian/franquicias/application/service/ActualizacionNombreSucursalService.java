package com.oscaradrian.franquicias.application.service;

import com.oscaradrian.franquicias.domain.usecase.ActualizacionNombreSucursalUseCase;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ActualizacionNombreSucursalService implements ActualizacionNombreSucursalUseCase {

    private final FranquiciaRepository repository;

    public ActualizacionNombreSucursalService(FranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Void> ejecutar(String nombreFranquicia, String nombreSucursalActual, String nuevoNombre) {
        return repository.findById(nombreFranquicia)
                .switchIfEmpty(Mono.error(new RecursoNoEncontrado("Franquicia no encontrada")))
                .flatMap(franquicia -> {
                    var sucursalOpt = franquicia.getSucursales().stream()
                            .filter(s -> s.getNombre().equalsIgnoreCase(nombreSucursalActual))
                            .findFirst();

                    if (sucursalOpt.isEmpty()) {
                        return Mono.error(new RecursoNoEncontrado("Sucursal no encontrada"));
                    }

                    sucursalOpt.get().setNombre(nuevoNombre);
                    return repository.save(franquicia).then();
                });
    }
}
