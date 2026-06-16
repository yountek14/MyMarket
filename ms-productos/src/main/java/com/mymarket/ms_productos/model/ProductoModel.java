package com.mymarket.ms_productos.model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Producto", description = "Representa un producto del catalogo")
public class ProductoModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    @Schema(description = "Nombre del producto", example = "Notebook Lenovo Thinkpad")
    private String nombreProducto;

    @NotBlank
    @Schema(description = "Categoria del producto", example = "Electronico")
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Unidad de medida del producto", example = "UNIDAD")
    private UnidadMedida unidadMedida;

    @NotNull
    @Schema(description = "Precio base del producto", example = "890000.0", minimum = "0")
    private Double precioBase;

    @Column(nullable = false)
    @Schema(description = "Indica si el producto esta activo", example = "true", defaultValue = "true")
    private Boolean activo;

    @Schema(description = "Descripcion del producto", example = "Notebook Lenovo Thinkpad i7 16GB RAM")
    private String descripcionProducto;

    @NotNull
    @Column(nullable = false)
    @Schema(description = "Fecha de caducidad del producto", example = "2027-06-01")
    private LocalDate fechaCaducidad;
}
