package com.mymarket.ms_precios.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "precios")
@Data @NoArgsConstructor @AllArgsConstructor
@Schema(name = "Precio", description = "Representa un precio registrado para un producto")
public class Precio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull
    @Schema(description = "ID del producto asociado", example = "1")
    private Long productoId;

    @NotNull
    @Schema(description = "Precio base del producto", example = "890000.0", minimum = "0")
    private Double precioBase;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo de descuento aplicado", example = "PORCENTAJE")
    private TipoDescuento tipoDescuento;

    @Schema(description = "Valor del descuento", example = "10.0")
    private Double valorDescuento;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Temporada asociada al precio", example = "VERANO")
    private Temporada temporada;

    @Schema(description = "Fecha de inicio de vigencia", example = "2026-01-01")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin de vigencia", example = "2026-03-31")
    private LocalDate fechaFin;

    @Schema(description = "Indica si el precio esta activo", example = "true", defaultValue = "true")
    private boolean activo = true;
}
