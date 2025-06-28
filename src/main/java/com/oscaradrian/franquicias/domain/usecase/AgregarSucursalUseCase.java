package com.oscaradrian.franquicias.domain.usecase;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.model.Sucursal;
import reactor.core.publisher.Mono;

public interface AgregarSucursalUseCase {
    Mono<Franquicia> ejecutar(String idFranquicia, Sucursal nuevaSucursal);
}