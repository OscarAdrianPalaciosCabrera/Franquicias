package com.oscaradrian.franquicias.service;

import com.oscaradrian.franquicias.application.service.AgregarSucursalService;
import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.model.Sucursal;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoYaExiste;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class AgregarSucursalServiceTest {

    @Test
    void debeAgregarSucursalCorrectamente() {

        FranquiciaRepository repositorio = mock(FranquiciaRepository.class);
        AgregarSucursalService servicio = new AgregarSucursalService(repositorio);

        Sucursal nueva = new Sucursal("Sucursal 1", List.of());
        Franquicia franquicia = new Franquicia("Exito", new ArrayList<>());

        when(repositorio.findById("Exito")).thenReturn(Mono.just(franquicia));
        when(repositorio.save(any())).thenReturn(Mono.just(franquicia));


        Mono<Franquicia> resultado = servicio.ejecutar("Exito", nueva);


        StepVerifier.create(resultado)
                .expectNextMatches(f -> f.getNombre().equals("Exito"))
                .verifyComplete();
    }

    @Test
    void debeFallarSiFranquiciaNoExiste() {

        FranquiciaRepository repositorio = mock(FranquiciaRepository.class);
        AgregarSucursalService servicio = new AgregarSucursalService(repositorio);

        Sucursal nueva = new Sucursal("Sucursal X", List.of());


        when(repositorio.findById("Inexistente")).thenReturn(Mono.empty());


        Mono<Franquicia> resultado = servicio.ejecutar("Inexistente", nueva);

        StepVerifier.create(resultado)
                .expectErrorMatches(throwable ->
                        throwable instanceof RecursoNoEncontrado &&
                                "La franquicia no existe".equals(throwable.getMessage())
                )

                .verify();
    }

    @Test
    void noDebeAgregarSucursalConNombreDuplicado() {
        // Arrange
        FranquiciaRepository repositorio = mock(FranquiciaRepository.class);
        AgregarSucursalService servicio = new AgregarSucursalService(repositorio);

        List<Sucursal> existentes = List.of(new Sucursal("Sucursal 1", List.of()));
        Franquicia franquicia = new Franquicia("Exito", new ArrayList<>(existentes));

        when(repositorio.findById("Exito")).thenReturn(Mono.just(franquicia));

        Sucursal duplicada = new Sucursal("Sucursal 1", List.of()); // mismo nombre que ya existe

        Mono<Franquicia> resultado = servicio.ejecutar("Exito", duplicada);

        StepVerifier.create(resultado)
                .expectErrorMatches(e ->
                        e instanceof RecursoYaExiste &&
                                e.getMessage().equals("Ya existe una sucursal con ese nombre"))
                .verify();

    }

    @Test
    void noDebePermitirIdFranquiciaNuloOVacio() {
        // Arrange
        FranquiciaRepository repositorio = mock(FranquiciaRepository.class);
        AgregarSucursalService servicio = new AgregarSucursalService(repositorio);

        Sucursal nuevaSucursal = new Sucursal("Sucursal Nueva", List.of());

        // Act
        Mono<Franquicia> resultadoConNull = servicio.ejecutar(null, nuevaSucursal);
        Mono<Franquicia> resultadoConVacio = servicio.ejecutar("", nuevaSucursal);

        // Assert
        StepVerifier.create(resultadoConNull)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("El ID de la franquicia no puede ser nulo ni vacío"))
                .verify();

        StepVerifier.create(resultadoConVacio)
                .expectErrorMatches(e -> e instanceof IllegalArgumentException &&
                        e.getMessage().equals("El ID de la franquicia no puede ser nulo ni vacío"))
                .verify();
    }

}
