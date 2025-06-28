package com.oscaradrian.franquicias.infrastructure.controller;

import com.oscaradrian.franquicias.application.DTOs.ActualizacionNombreFranquiciaDTO;

import com.oscaradrian.franquicias.domain.model.Franquicia;

import com.oscaradrian.franquicias.domain.usecase.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/franquicias")
public class FranquiciaController {

    private final AgregarFranquiciaUseCase agregarFranquicia;
    private final ObtenerFranquiciasUseCase obtenerFranquicias;
    private final ActualizacionNombreFranquiciaUseCase actualizacionNombreFranquicia;
    private final ProductoMayorStockUseCase productoMayorStock;

    public FranquiciaController(AgregarFranquiciaUseCase agregarFranquicia,
                                ObtenerFranquiciasUseCase obtenerFranquicias,
                                ActualizacionNombreFranquiciaUseCase actualizacionNombreFranquicia,
                                ProductoMayorStockUseCase productoMayorStock) {

        this.agregarFranquicia = agregarFranquicia;
        this.obtenerFranquicias = obtenerFranquicias;
        this.actualizacionNombreFranquicia = actualizacionNombreFranquicia;
        this.productoMayorStock = productoMayorStock;

    }

    @GetMapping
    public Flux<Franquicia> obtenerFranquicias() {
        return obtenerFranquicias.ejecutar();
    }

    @PostMapping
    public Mono<Franquicia> crearFranquicia(@RequestBody Franquicia franquicia) {
        return agregarFranquicia.ejecutar(franquicia);
    }

    @PutMapping("/{nombreActual}/actualizar-nombre")
    public Mono<ResponseEntity<Franquicia>> actualizacionNombreFranquicia(
            @PathVariable String nombreActual,
            @RequestBody ActualizacionNombreFranquiciaDTO dto) {

        return actualizacionNombreFranquicia.ejecutar(nombreActual, dto.getNuevoNombre())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{nombreFranquicia}/productos-mayor-stock")
    public Mono<ResponseEntity<List<String>>> obtenerProductosConMayorStock(
            @PathVariable("nombreFranquicia") String nombreFranquicia) {
        return productoMayorStock.ejecutar(nombreFranquicia)
                .map(ResponseEntity::ok);
    }
}