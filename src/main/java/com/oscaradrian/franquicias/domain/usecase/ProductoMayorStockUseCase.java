package com.oscaradrian.franquicias.domain.usecase;

import com.oscaradrian.franquicias.application.DTOs.SucursalYProductoDTO;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductoMayorStockUseCase {
        Mono<List<String>> ejecutar(String nombreFranquicia);
}
