package com.oscaradrian.franquicias.domain.usecase;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.model.Producto;
import reactor.core.publisher.Mono;

public interface AgregarProductoUseCase {
    Mono<Franquicia> ejecutar(String idFranquicia, String nombreSucursal, Producto producto);

}
