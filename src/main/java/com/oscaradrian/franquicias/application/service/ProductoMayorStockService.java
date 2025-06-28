package com.oscaradrian.franquicias.application.service;

import com.oscaradrian.franquicias.application.DTOs.SucursalYProductoDTO;
import com.oscaradrian.franquicias.domain.usecase.ProductoMayorStockUseCase;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import com.oscaradrian.franquicias.domain.model.Producto;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductoMayorStockService implements ProductoMayorStockUseCase {

    private final FranquiciaRepository repository;

    public ProductoMayorStockService(FranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<List<String>> ejecutar(String nombreFranquicia) {
        return repository.findById(nombreFranquicia)
                .switchIfEmpty(Mono.error(new RecursoNoEncontrado("Franquicia no encontrada")))
                .flatMap(franquicia -> {
                    if (franquicia.getSucursales() == null || franquicia.getSucursales().isEmpty()) {
                        return Mono.error(new RecursoNoEncontrado("La franquicia no tiene sucursales"));
                    }

                    List<String> mensajes = franquicia.getSucursales().stream()
                            .filter(s -> s.getProductos() != null && !s.getProductos().isEmpty())
                            .map(sucursal -> sucursal.getProductos().stream()
                                    .filter(p -> p.getNombre() != null)
                                    .max(Comparator.comparingInt(Producto::getStock))
                                    .map(producto -> "En " + sucursal.getNombre() +
                                            ", el producto con más stock es: " + producto.getNombre() +
                                            " (Stock: " + producto.getStock() + ")")
                                    .orElse(null))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());

                    if (mensajes.isEmpty()) {
                        return Mono.error(new RecursoNoEncontrado("No hay productos con stock en ninguna sucursal"));
                    }

                    return Mono.just(mensajes);
                });
    }
}

