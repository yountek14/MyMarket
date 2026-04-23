package com.mymarket.ms_productos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "productos")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String nombreProducto;

    @NotBlank
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UnidadMedida unidadMedida;

    @NotNull
    private Double precioBase;

    @Column(nullable = false)
    private Boolean activo;

    private String descripcionProducto;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaCaducidad;


}
