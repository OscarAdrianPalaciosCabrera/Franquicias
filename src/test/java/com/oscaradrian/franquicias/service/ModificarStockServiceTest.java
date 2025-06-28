package com.oscaradrian.franquicias.service;

import com.oscaradrian.franquicias.application.service.ModificarStockService;
import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.model.Producto;
import com.oscaradrian.franquicias.domain.model.Sucursal;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModificarStockServiceTest {

    private FranquiciaRepository repository;
    private ModificarStockService service;

    @BeforeEach
    void setUp() {
        repository = mock(FranquiciaRepository.class);
        service = new ModificarStockService(repository);
    }

    @Test
    void debeActualizarElStockCorrectamente() {
        // Arrange
        Producto producto = new Producto("Pizza", 5);
        Sucursal sucursal = new Sucursal("Sucursal1", List.of(producto));
        Franquicia franquicia = new Franquicia("Dominos", List.of(sucursal));

        when(repository.findById("Dominos")).thenReturn(Mono.just(franquicia));
        when(repository.save(franquicia)).thenReturn(Mono.just(franquicia));

        // Act & Assert
        StepVerifier.create(service.ejecutar("Dominos", "Sucursal1", "Pizza", 20))
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCodeValue());
                    assertEquals("Stock del producto actualizado correctamente", response.getBody().get("mensaje"));
                    assertEquals(20, producto.getStock());
                })
                .verifyComplete();

        verify(repository).findById("Dominos");
        verify(repository).save(franquicia);
    }

    @Test
    void debeLanzarErrorSiLaFranquiciaNoExiste() {
        when(repository.findById("Inexistente")).thenReturn(Mono.empty());

        StepVerifier.create(service.ejecutar("Inexistente", "Sucursal1", "Pizza", 10))
                .expectErrorMatches(error ->
                        error instanceof RecursoNoEncontrado &&
                                error.getMessage().equals("Franquicia no encontrada"))
                .verify();
    }

    @Test
    void debeLanzarErrorSiLaSucursalNoExiste() {
        Franquicia franquicia = new Franquicia("Dominos", List.of());
        when(repository.findById("Dominos")).thenReturn(Mono.just(franquicia));

        StepVerifier.create(service.ejecutar("Dominos", "SucursalInvalida", "Pizza", 10))
                .expectErrorMatches(error ->
                        error instanceof RecursoNoEncontrado &&
                                error.getMessage().equals("Sucursal no encontrada"))
                .verify();
    }

    @Test
    void debeLanzarErrorSiElProductoNoExiste() {
        Sucursal sucursal = new Sucursal("Sucursal1", List.of());
        Franquicia franquicia = new Franquicia("Dominos", List.of(sucursal));
        when(repository.findById("Dominos")).thenReturn(Mono.just(franquicia));

        StepVerifier.create(service.ejecutar("Dominos", "Sucursal1", "Pizza", 10))
                .expectErrorMatches(error ->
                        error instanceof RecursoNoEncontrado &&
                                error.getMessage().equals("Producto no encontrado"))
                .verify();
    }
}
