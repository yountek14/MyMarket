package com.mymarket.ms_ventas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Venta", description = "Representa una venta registrada en el sistema")
public class VentaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio.")
    @Column(nullable = false)
    @Schema(description = "ID del producto vendido", example = "1")
    private Long productoId;

    @NotNull(message = "El ID del inventario es obligatorio.")
    @Column(nullable = false)
    @Schema(description = "ID del registro de inventario", example = "1")
    private Long inventarioId;

    @NotNull(message = "La cantidad vendida es obligatoria.")
    @Positive(message = "La cantidad vendida debe ser mayor a 0.")
    @Column(nullable = false)
    @Schema(description = "Cantidad vendida del producto", example = "2", minimum = "1")
    private Integer cantidadVendida;

    @NotNull(message = "El precio unitario es obligatorio.")
    @Positive(message = "El precio unitario debe ser mayor a 0.")
    @Column(nullable = false)
    @Schema(description = "Precio unitario del producto", example = "890000.0", minimum = "0")
    private Double precioUnitario;

    @Column(nullable = false)
    @Schema(description = "Total de la venta", example = "1780000.0")
    private Double totalVenta;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora de la venta", example = "2026-06-09T15:30:00")
    private LocalDateTime fechaVenta;

    @Column(nullable = false)
    @Schema(description = "Indica si la venta esta activa", example = "true", defaultValue = "true")
    private Boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado de la venta", example = "COMPLETADA")
    private EstadoVenta estado;
}