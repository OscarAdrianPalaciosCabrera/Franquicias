package com.oscaradrian.franquicias.application.service;

import com.oscaradrian.franquicias.domain.usecase.ActualizacionNombreProductoUseCase;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ActualizacionNombreProductoService implements ActualizacionNombreProductoUseCase {

    private final FranquiciaRepository repository;

    public ActualizacionNombreProductoService(FranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Void> ejecutar(String nombreFranquicia, String nombreSucursal, String nombreProductoActual, String nuevoNombre) {
        return repository.findById(nombreFranquicia)
                .switchIfEmpty(Mono.error(new RecursoNoEncontrado("Franquicia no encontrada")))
                .flatMap(franquicia -> {
                    var sucursalOpt = franquicia.getSucursales().stream()
                            .filter(s -> s.getNombre().equalsIgnoreCase(nombreSucursal))
                            .findFirst();

                    if (sucursalOpt.isEmpty()) {
                        return Mono.error(new RecursoNoEncontrado("Sucursal no encontrada"));
                    }

                    var productoOpt = sucursalOpt.get().getProductos().stream()
                            .filter(p -> p.getNombre().equalsIgnoreCase(nombreProductoActual))
                            .findFirst();

                    if (productoOpt.isEmpty()) {
                        return Mono.error(new RecursoNoEncontrado("Producto no encontrado"));
                    }

                    productoOpt.get().setNombre(nuevoNombre);
                    return repository.save(franquicia).then();
                });
    }
}
