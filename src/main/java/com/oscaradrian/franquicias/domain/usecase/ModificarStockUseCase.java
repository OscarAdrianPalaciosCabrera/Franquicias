package com.oscaradrian.franquicias.domain.usecase;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ModificarStockUseCase {
    Mono<ResponseEntity<Map<String, String>>> ejecutar(String idFranquicia, String nombreSucursal, String nombreProducto, int nuevoStock);
}
