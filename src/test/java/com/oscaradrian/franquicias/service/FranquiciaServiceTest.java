package com.oscaradrian.franquicias.service;

import com.oscaradrian.franquicias.application.service.FranquiciaService;
import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoYaExiste;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class FranquiciaServiceTest {

    @Test
    void debeGuardarFranquiciaCorrectamente() {
        // Arrange
        FranquiciaRepository repo = mock(FranquiciaRepository.class);
        FranquiciaService servicio = new FranquiciaService(repo);

        Franquicia nueva = new Franquicia("Burger King", List.of());

        when(repo.findById("Burger King")).thenReturn(Mono.empty());
        when(repo.save(nueva)).thenReturn(Mono.just(nueva));

        // Act
        Mono<Franquicia> resultado = servicio.ejecutar(nueva);

        // Assert
        StepVerifier.create(resultado)
                .expectNextMatches(f -> f.getNombre().equals("Burger King"))
                .verifyComplete();
    }

    @Test
    void noDebeGuardarFranquiciaSiYaExiste() {
        // Arrange
        FranquiciaRepository repo = mock(FranquiciaRepository.class);
        FranquiciaService servicio = new FranquiciaService(repo);

        Franquicia existente = new Franquicia("Dominos", List.of());

        when(repo.findById("Dominos")).thenReturn(Mono.just(existente));

        // Act
        Mono<Franquicia> resultado = servicio.ejecutar(existente);

        // Assert
        StepVerifier.create(resultado)
                .expectErrorMatches(error ->
                        error instanceof RecursoYaExiste &&
                                error.getMessage().equals("Ya existe una franquicia con ese nombre"))
                .verify();

        // Verify que nunca se intentó guardar
        verify(repo, never()).save(any());
    }


    @Test
    void debeListarTodasLasFranquicias() {
        // Arrange
        FranquiciaRepository repo = mock(FranquiciaRepository.class);
        FranquiciaService servicio = new FranquiciaService(repo);

        Franquicia f1 = new Franquicia("Exito", List.of());
        Franquicia f2 = new Franquicia("KFC", List.of());

        when(repo.findAll()).thenReturn(Flux.just(f1, f2));

        // Act
        Flux<Franquicia> resultado = servicio.ejecutar();

        // Assert
        StepVerifier.create(resultado)
                .expectNext(f1)
                .expectNext(f2)
                .verifyComplete();
    }
}
