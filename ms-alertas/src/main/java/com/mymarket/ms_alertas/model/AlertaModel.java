package com.mymarket.ms_alertas.model;

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
public class AlertaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del producto es obligatorio.")
    @Column(nullable = false)
    private Long productoId;

    @NotNull(message = "El ID del inventario es obligatorio.")
    @Column(nullable = false)
    private Long inventarioId;

    @NotNull(message = "El tipo de alerta es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAlerta tipoAlerta;

    @NotNull(message = "El estado de alerta es obligatorio.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlerta estadoAlerta;

    @NotBlank(message = "El mensaje de alerta es obligatorio.")
    @Column(nullable = false, length = 255)
    private String mensaje;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaResolucion;

    @Column(nullable = false)
    private Boolean activo;
}