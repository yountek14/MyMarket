package com.mymarket.ms_ventas.model;

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
public class VentaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio.")
    @Column(nullable = false)
    private Long productoId;

    @NotNull(message = "El ID del inventario es obligatorio.")
    @Column(nullable = false)
    private Long inventarioId;

    @NotNull(message = "La cantidad vendida es obligatoria.")
    @Positive(message = "La cantidad vendida debe ser mayor a 0.")
    @Column(nullable = false)
    private Integer cantidadVendida;

    @NotNull(message = "El precio unitario es obligatorio.")
    @Positive(message = "El precio unitario debe ser mayor a 0.")
    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double totalVenta;

    @Column(nullable = false)
    private LocalDateTime fechaVenta;

    @Column(nullable = false)
    private Boolean activo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoVenta estado;
}