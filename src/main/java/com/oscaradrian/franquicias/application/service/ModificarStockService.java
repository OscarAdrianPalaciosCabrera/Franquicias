package com.oscaradrian.franquicias.application.service;
import com.oscaradrian.franquicias.domain.usecase.ModificarStockUseCase;

import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
public class ModificarStockService implements ModificarStockUseCase {
    private final FranquiciaRepository repository;

    public ModificarStockService(FranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<ResponseEntity<Map<String, String>>> ejecutar(String idFranquicia, String nombreSucursal, String nombreProducto, int nuevoStock) {
        return repository.findById(idFranquicia)
                .switchIfEmpty(Mono.error(new RecursoNoEncontrado("Franquicia no encontrada")))
                .flatMap(franquicia -> {
                    if (franquicia.getSucursales() == null) {
                        return Mono.error(new RecursoNoEncontrado("La franquicia no tiene sucursales"));
                    }

                    var sucursalOpt = franquicia.getSucursales().stream()
                            .filter(s -> s.getNombre() != null && s.getNombre().equalsIgnoreCase(nombreSucursal))
                            .findFirst();

                    if (sucursalOpt.isEmpty()) {
                        return Mono.error(new RecursoNoEncontrado("Sucursal no encontrada"));
                    }

                    var sucursal = sucursalOpt.get();

                    if (sucursal.getProductos() == null) {
                        return Mono.error(new RecursoNoEncontrado("La sucursal no tiene productos"));
                    }

                    var productoOpt = sucursal.getProductos().stream()
                            .filter(p -> p.getNombre() != null && p.getNombre().equalsIgnoreCase(nombreProducto))
                            .findFirst();

                    if (productoOpt.isEmpty()) {
                        return Mono.error(new RecursoNoEncontrado("Producto no encontrado"));
                    }

                    productoOpt.get().setStock(nuevoStock);

                    return repository.save(franquicia)
                            .then(Mono.fromSupplier(() -> {
                                Map<String, String> respuesta = new HashMap<>();
                                respuesta.put("mensaje", "Stock del producto actualizado correctamente");
                                return ResponseEntity.ok(respuesta);
                            }));
                });
    }
}



