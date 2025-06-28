package com.oscaradrian.franquicias.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;


@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Franquicia {
    @Id
    private String nombre;
    private List<Sucursal> sucursales;
}
