package com.oscaradrian.franquicias.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    @Id
    private String nombre;
    private int stock;

    // Getters and Setters
}
