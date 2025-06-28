package com.oscaradrian.franquicias.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sucursal {
    @Id
    private String nombre;
    private List<Producto> productos;
}