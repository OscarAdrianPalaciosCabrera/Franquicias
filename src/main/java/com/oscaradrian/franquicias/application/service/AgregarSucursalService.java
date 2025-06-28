package com.oscaradrian.franquicias.application.service;

import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.model.Sucursal;
import com.oscaradrian.franquicias.domain.usecase.AgregarSucursalUseCase;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoYaExiste;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
public class AgregarSucursalService implements AgregarSucursalUseCase {

    private final FranquiciaRepository repository;

    public AgregarSucursalService(FranquiciaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Franquicia> ejecutar(String nombreFranquicia, Sucursal nuevaSucursal) {
        if (nombreFranquicia == null || nombreFranquicia.trim().isEmpty()) {
            return Mono.error(new IllegalArgumentException("El ID de la franquicia no puede ser nulo ni vacío"));
        }

        return repository.findById(nombreFranquicia)
                .switchIfEmpty(Mono.error(new RecursoNoEncontrado("La franquicia no existe")))
                .flatMap(franquicia -> {
                    if (franquicia.getSucursales() == null) {
                        franquicia.setSucursales(new ArrayList<>());
                    }


                    boolean yaExiste = franquicia.getSucursales().stream()
                            .anyMatch(s -> s.getNombre().equalsIgnoreCase(nuevaSucursal.getNombre()));

                    if (yaExiste) {
                        return Mono.error(new RecursoYaExiste("Ya existe una sucursal con ese nombre"));
                    }

                    franquicia.getSucursales().add(nuevaSucursal);
                    return repository.save(franquicia);
                });
    }
}