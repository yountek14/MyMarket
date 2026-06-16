package com.mymarket.ms_empleados.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "empleados")
@Data @NoArgsConstructor @AllArgsConstructor
@Schema(name = "Empleado", description = "Representa un empleado registrado en el sistema")
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Schema(description = "Nombre del empleado", example = "Juan")
    private String nombre;

    @NotBlank
    @Schema(description = "Apellido del empleado", example = "Perez")
    private String apellido;

    @NotBlank
    @Column(unique = true)
    @Schema(description = "RUT del empleado", example = "12.345.678-9")
    private String rut;

    @NotBlank
    @Schema(description = "Telefono de contacto", example = "+56 9 1234 5678")
    private String telefono;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Rol del empleado", example = "VENDEDOR")
    private Rol rol;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Schema(description = "Turno del empleado", example = "MANANA")
    private Turno turno;

    @NotNull
    @Schema(description = "Fecha de contratacion", example = "2026-01-15")
    private LocalDate fechaContratacion;

    @Schema(description = "Indica si el empleado esta activo", example = "true", defaultValue = "true")
    private boolean activo = true;

    @NotNull
    @Schema(description = "ID del usuario asociado", example = "1")
    private Long usuarioId;
}
