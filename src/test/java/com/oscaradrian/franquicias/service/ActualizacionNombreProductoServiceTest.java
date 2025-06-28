package com.oscaradrian.franquicias.service;

import com.oscaradrian.franquicias.application.service.ActualizacionNombreProductoService;
import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.model.Producto;
import com.oscaradrian.franquicias.domain.model.Sucursal;
import com.oscaradrian.franquicias.infrastructure.exception.RecursoNoEncontrado;
import com.oscaradrian.franquicias.infrastructure.repository.FranquiciaRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.*;

class ActualizacionNombreProductoServiceTest {

    @Test
    void debeActualizarNombreDelProducto() {
        // Arrange
        FranquiciaRepository repo = mock(FranquiciaRepository.class);
        ActualizacionNombreProductoService service = new ActualizacionNombreProductoService(repo);

        var producto = new Producto("Pizza", 10); // Por ejemplo, stock 10
        var sucursal = new Sucursal("Sucursal A", List.of(producto));
        var franquicia = new Franquicia("Dominos", List.of(sucursal));

        when(repo.findById("Dominos")).thenReturn(Mono.just(franquicia));
        when(repo.save(any(Franquicia.class))).thenReturn(Mono.just(franquicia));

        // Act
        Mono<Void> resultado = service.ejecutar("Dominos", "Sucursal A", "Pizza", "Pizza Vegetariana");

        // Assert
        StepVerifier.create(resultado)
                .verifyComplete();

        assert producto.getNombre().equals("Pizza Vegetariana");
        verify(repo).save(franquicia);
    }

    @Test
    void debeFallarSiFranquiciaNoExiste() {
        // Arrange
        FranquiciaRepository repo = mock(FranquiciaRepository.class);
        ActualizacionNombreProductoService service = new ActualizacionNombreProductoService(repo);

        when(repo.findById("Inexistente")).thenReturn(Mono.empty());

        // Act
        Mono<Void> resultado = service.ejecutar("Inexistente", "Sucursal A", "Pizza", "Otra Pizza");

        // Assert
        StepVerifier.create(resultado)
                .expectErrorMatches(e ->
                        e instanceof RecursoNoEncontrado &&
                                e.getMessage().equals("Franquicia no encontrada"))
                .verify();
    }

    @Test
    void debeFallarSiSucursalNoExiste() {
        // Arrange
        FranquiciaRepository repo = mock(FranquiciaRepository.class);
        ActualizacionNombreProductoService service = new ActualizacionNombreProductoService(repo);

        var franquicia = new Franquicia("Dominos", List.of()); // sin sucursales
        when(repo.findById("Dominos")).thenReturn(Mono.just(franquicia));

        // Act
        Mono<Void> resultado = service.ejecutar("Dominos", "Sucursal Inexistente", "Pizza", "Pizza Nueva");

        // Assert
        StepVerifier.create(resultado)
                .expectErrorMatches(e ->
                        e instanceof RecursoNoEncontrado &&
                                e.getMessage().equals("Sucursal no encontrada"))
                .verify();
    }

    @Test
    void debeFallarSiProductoNoExiste() {
        // Arrange
        FranquiciaRepository repo = mock(FranquiciaRepository.class);
        ActualizacionNombreProductoService service = new ActualizacionNombreProductoService(repo);

        var sucursal = new Sucursal("Sucursal A", List.of()); // sin productos
        var franquicia = new Franquicia("Dominos", List.of(sucursal));

        when(repo.findById("Dominos")).thenReturn(Mono.just(franquicia));

        // Act
        Mono<Void> resultado = service.ejecutar("Dominos", "Sucursal A", "Producto Inexistente", "Nuevo");

        // Assert
        StepVerifier.create(resultado)
                .expectErrorMatches(e ->
                        e instanceof RecursoNoEncontrado &&
                                e.getMessage().equals("Producto no encontrado"))
                .verify();
    }
}
