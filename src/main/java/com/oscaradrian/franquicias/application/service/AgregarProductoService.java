package com.oscaradrian.franquicias.application.service;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.model.Producto;
import com.oscaradrian.franquicias.domain.model.Sucursal;
import com.oscaradrian.franquicias.domain.usecase.AgregarProductoUseCase;
import com.oscaradrian.franquicias.infrastructure.exception.DatoInvalido;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoYaExiste;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class AgregarProductoService implements AgregarProductoUseCase {

    private final FranquiciaRepository repository;

    public AgregarProductoService(FranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Franquicia> ejecutar(String idFranquicia, String nombreSucursal, Producto producto) {
        return repository.findById(idFranquicia)
                .switchIfEmpty(Mono.error(new RecursoNoEncontrado("La franquicia no existe")))
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
                                if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                                    return Mono.error(new DatoInvalido("El nombre del producto no puede ser vacío o nulo"));
                                }

                                if (sucursal.getProductos() == null) {
                                    sucursal.setProductos(new ArrayList<>());
                                }

                                boolean existeProducto = sucursal.getProductos().stream()
                                        .anyMatch(p -> p.getNombre() != null &&
                                                p.getNombre().equalsIgnoreCase(producto.getNombre()));

                                if (existeProducto) {
                                    return Mono.error(new RecursoYaExiste("Ya existe un producto con ese nombre en la sucursal"));
                                }

                                sucursal.getProductos().add(producto);
                                return repository.save(franquicia);
                            });
                });
        }
    }

