package com.oscaradrian.franquicias.infrastructure.controller;

import com.oscaradrian.franquicias.application.DTOs.ActualizacionNombreSucursalDTO;
import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.model.Sucursal;
import com.oscaradrian.franquicias.domain.usecase.ActualizacionNombreSucursalUseCase;
import com.oscaradrian.franquicias.domain.usecase.AgregarSucursalUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/franquicias/{nombreFranquicia}/sucursales")
public class SucursalController {
    private final AgregarSucursalUseCase agregarSucursal;
    private final ActualizacionNombreSucursalUseCase actualizacionNombreSucursal;

    public SucursalController(AgregarSucursalUseCase agregarSucursal,
                              ActualizacionNombreSucursalUseCase actualizacionNombreSucursal){
        this.agregarSucursal = agregarSucursal;
        this.actualizacionNombreSucursal = actualizacionNombreSucursal;
    }

    @PostMapping
    public Mono<Franquicia> agregarSucursal(
            @PathVariable("nombreFranquicia") String nombreFranquicia,
            @RequestBody Sucursal sucursal) {
        return agregarSucursal.ejecutar(nombreFranquicia, sucursal);
    }

    @PutMapping("/{nombreSucursalActual}/actualizar-nombre")
    public Mono<ResponseEntity<String>> actualizarNombreSucursal(
            @PathVariable String nombreFranquicia,
            @PathVariable String nombreSucursalActual,
            @RequestBody ActualizacionNombreSucursalDTO dto) {

        return actualizacionNombreSucursal.ejecutar(nombreFranquicia, nombreSucursalActual, dto.getNuevoNombre())
                .thenReturn(ResponseEntity.ok("Nombre de la sucursal actualizado correctamente"));
    }

}
