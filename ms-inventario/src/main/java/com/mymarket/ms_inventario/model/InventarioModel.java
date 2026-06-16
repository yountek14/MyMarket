package com.mymarket.ms_inventario.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "inventario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Inventario", description = "Representa un registro de inventario con control de stock y lotes")
public class InventarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio.")
    @Column(nullable = false)
    @Schema(description = "ID del producto asociado", example = "1")
    private Long productoId;

    @NotBlank(message = "El lote es obligatorio.")
    @Column(nullable = false, length = 100)
    @Schema(description = "Numero de lote del producto", example = "LOTE-2025-001", minLength = 3, maxLength = 100)
    private String lote;

    @NotNull(message = "El stock actual es obligatorio.")
    @PositiveOrZero(message = "El stock actual no puede ser negativo.")
    @Column(nullable = false)
    @Schema(description = "Cantidad actual en stock", example = "150", minimum = "0")
    private Integer stockActual;

    @NotNull(message = "El stock mínimo es obligatorio.")
    @PositiveOrZero(message = "El stock mínimo no puede ser negativo.")
    @Column(nullable = false)
    @Schema(description = "Stock minimo permitido", example = "10", minimum = "0")
    private Integer stockMinimo;

    @NotNull(message = "El stock máximo es obligatorio.")
    @PositiveOrZero(message = "El stock máximo no puede ser negativo.")
    @Column(nullable = false)
    @Schema(description = "Stock maximo permitido", example = "500", minimum = "0")
    private Integer stockMaximo;

    @NotNull(message = "La merma es obligatoria.")
    @PositiveOrZero(message = "La merma no puede ser negativa.")
    @Column(nullable = false)
    @Schema(description = "Cantidad de merma registrada", example = "0", minimum = "0")
    private Integer merma;

    @NotNull(message = "La fecha de ingreso es obligatoria.")
    @Column(nullable = false)
    @Schema(description = "Fecha de ingreso del producto", example = "2026-01-15")
    private LocalDate fechaIngreso;

    @NotNull(message = "La fecha de vencimiento es obligatoria.")
    @Column(nullable = false)
    @Schema(description = "Fecha de vencimiento del producto", example = "2026-12-31")
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado del inventario", example = "DISPONIBLE")
    private EstadoInventario estado;

    @Column(nullable = false)
    @Schema(description = "Indica si el registro esta activo", example = "true", defaultValue = "true")
    private Boolean activo;
}