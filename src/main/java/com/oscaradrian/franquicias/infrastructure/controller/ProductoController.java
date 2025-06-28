package com.oscaradrian.franquicias.infrastructure.controller;

import com.oscaradrian.franquicias.application.DTOs.ActualizacionNombreProductoDTO;
import com.oscaradrian.franquicias.domain.model.Franquicia;
import com.oscaradrian.franquicias.domain.model.Producto;
import com.oscaradrian.franquicias.domain.usecase.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/franquicias/{nombreFranquicia}/sucursales/{nombreSucursal}/productos")
public class ProductoController {
    private final AgregarProductoUseCase agregarProducto;
    private final EliminarProductoUseCase eliminarProducto;
    private final ModificarStockUseCase modificarStock;
    private final ActualizacionNombreProductoUseCase actualizacionNombreProducto;

    public ProductoController(AgregarProductoUseCase agregarProducto,
                              EliminarProductoUseCase eliminarProducto,
                              ModificarStockUseCase modificarStock,
                              ActualizacionNombreProductoUseCase actualizacionNombreProducto){

        this.agregarProducto = agregarProducto;
        this.eliminarProducto =eliminarProducto;
        this.modificarStock = modificarStock;
        this.actualizacionNombreProducto = actualizacionNombreProducto;


    }

    @PostMapping
    public Mono<Franquicia> agregarProducto(
            @PathVariable("nombreFranquicia") String nombreFranquicia,
            @PathVariable("nombreSucursal") String nombreSucursal,
            @RequestBody Producto producto) {
        return agregarProducto.ejecutar(nombreFranquicia, nombreSucursal, producto);
    }

    @DeleteMapping("/{producto}")
    public Mono<ResponseEntity<Map<String, String>>> eliminarProducto(
            @PathVariable("nombreFranquicia") String nombreFranquicia,
            @PathVariable("nombreSucursal") String nombreSucursal ,
            @PathVariable("producto") String producto) {

        return eliminarProducto.ejecutar(nombreFranquicia, nombreSucursal, producto);
    }

    @PutMapping("/{producto}/stock")
    public Mono<ResponseEntity<Map<String, String>>> modificarStock(
            @PathVariable("nombreFranquicia") String nombreFranquicia,
            @PathVariable("nombreSucursal") String nombreSucursal,
            @PathVariable("producto") String producto,
            @RequestParam("stock") int nuevoStock) {
        return modificarStock.ejecutar(nombreFranquicia, nombreSucursal, producto, nuevoStock);
    }

    @PutMapping("/{nombreProductoActual}/actualizar-nombre")
    public Mono<ResponseEntity<String>> actualizarNombreProducto(
            @PathVariable String nombreFranquicia,
            @PathVariable String nombreSucursal,
            @PathVariable String nombreProductoActual,
            @RequestBody ActualizacionNombreProductoDTO dto) {

        return actualizacionNombreProducto.ejecutar(nombreFranquicia, nombreSucursal, nombreProductoActual, dto.getNuevoNombre())
                .thenReturn(ResponseEntity.ok("Nombre del producto actualizado correctamente"));
    }
}
