package com.mymarket.ms_alertas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "alertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "Alerta", description = "Representa una alerta generada en el sistema")
public class AlertaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio.")
    @Column(nullable = false)
    @Schema(description = "ID del producto asociado", example = "1")
    private Long productoId;

    @NotNull(message = "El ID del inventario es obligatorio.")
    @Column(nullable = false)
    @Schema(description = "ID del registro de inventario", example = "1")
    private Long inventarioId;

    @NotNull(message = "El tipo de alerta es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Tipo de alerta", example = "STOCK")
    private TipoAlerta tipoAlerta;

    @NotNull(message = "El estado de alerta es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Estado actual de la alerta", example = "ACTIVA")
    private EstadoAlerta estadoAlerta;

    @NotBlank(message = "El mensaje de alerta es obligatorio.")
    @Column(nullable = false, length = 255)
    @Schema(description = "Mensaje descriptivo de la alerta", example = "Stock bajo para el producto Notebook Lenovo")
    private String mensaje;

    @Column(nullable = false)
    @Schema(description = "Fecha y hora de creacion", example = "2026-06-09T10:30:00")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha y hora de resolucion", example = "2026-06-09T12:00:00")
    private LocalDateTime fechaResolucion;

    @Column(nullable = false)
    @Schema(description = "Indica si la alerta esta activa", example = "true", defaultValue = "true")
    private Boolean activo;
}