package com.oscaradrian.franquicias.application.service;

import com.oscaradrian.franquicias.domain.usecase.EliminarProductoUseCase;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class EliminarProductoService implements EliminarProductoUseCase {
    private final FranquiciaRepository repository;

    public EliminarProductoService(FranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<ResponseEntity<Map<String, String>>> ejecutar(String nombreFranquicia, String nombreSucursal, String nombreProducto) {
        return repository.findById(nombreFranquicia)
                .switchIfEmpty(Mono.error(new RecursoNoEncontrado("Franquicia no encontrada")))
                .flatMap(franquicia -> {
                    if (franquicia.getSucursales() == null) {
                        return Mono.error(new RecursoNoEncontrado("No hay sucursales en la franquicia"));
                    }

                    return Mono.justOrEmpty(
                                    franquicia.getSucursales().stream()
                                            .filter(s -> s.getNombre() != null &&
                                                    s.getNombre().equalsIgnoreCase(nombreSucursal))
                                            .findFirst()
                            ).switchIfEmpty(Mono.error(new RecursoNoEncontrado("Sucursal no encontrada")))
                            .flatMap(sucursal -> {
                                if (sucursal.getProductos() == null) {
                                    return Mono.error(new RecursoNoEncontrado("La sucursal no tiene productos"));
                                }

                                boolean removed = sucursal.getProductos().removeIf(
                                        p -> p.getNombre() != null &&
                                                p.getNombre().equalsIgnoreCase(nombreProducto)
                                );

                                if (!removed) {
                                    return Mono.error(new RecursoNoEncontrado("Producto no encontrado en la sucursal"));
                                }

                                return repository.save(franquicia)
                                        .thenReturn(ResponseEntity.ok(
                                                Map.of("mensaje", "Producto eliminado correctamente")
                                        ));
                            });
                });
    }
}

