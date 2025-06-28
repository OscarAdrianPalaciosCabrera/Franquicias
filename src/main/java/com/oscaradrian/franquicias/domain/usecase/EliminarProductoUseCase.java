package com.oscaradrian.franquicias.domain.usecase;

import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface EliminarProductoUseCase {
    Mono<ResponseEntity<Map<String, String>>> ejecutar(String nombreFranquicia, String nombreSucursal, String nombreProducto);
}
