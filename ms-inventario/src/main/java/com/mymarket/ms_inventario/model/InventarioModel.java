package com.mymarket.ms_inventario.model;

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
public class InventarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID del producto que viene desde ms-productos
    @NotNull(message = "El ID del producto es obligatorio.")
    @Column(nullable = false)
    private Long productoId;

    @NotBlank(message = "El lote es obligatorio.")
    @Column(nullable = false, length = 50)
    private String lote;

    @NotNull(message = "El stock actual es obligatorio.")
    @PositiveOrZero(message = "El stock actual no puede ser negativo.")
    @Column(nullable = false)
    private Integer stockActual;

    @NotNull(message = "El stock mínimo es obligatorio.")
    @PositiveOrZero(message = "El stock mínimo no puede ser negativo.")
    @Column(nullable = false)
    private Integer stockMinimo;

    @NotNull(message = "El stock máximo es obligatorio.")
    @PositiveOrZero(message = "El stock máximo no puede ser negativo.")
    @Column(nullable = false)
    private Integer stockMaximo;

    @NotNull(message = "La merma es obligatoria.")
    @PositiveOrZero(message = "La merma no puede ser negativa.")
    @Column(nullable = false)
    private Integer merma;

    @NotNull(message = "La fecha de ingreso es obligatoria.")
    @Column(nullable = false)
    private LocalDate fechaIngreso;

    @NotNull(message = "La fecha de vencimiento es obligatoria.")
    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoInventario estado;

    @Column(nullable = false)
    private Boolean activo;
}