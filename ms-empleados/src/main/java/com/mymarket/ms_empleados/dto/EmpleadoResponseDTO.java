package com.mymarket.ms_empleados.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mymarket.ms_empleados.model.Rol;
import com.mymarket.ms_empleados.model.Turno;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EmpleadoResponse", description = "DTO de respuesta con datos del empleado")
public class EmpleadoResponseDTO {
    @Schema(title = "Identificador unico", example = "1")
    private Long id;

    @Schema(description = "Nombre del empleado", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido del empleado", example = "Perez")
    private String apellido;

    @Schema(description = "RUT del empleado", example = "12.345.678-9")
    private String rut;

    @Schema(description = "Telefono de contacto", example = "+56 9 1234 5678")
    private String telefono;

    @Schema(description = "Rol del empleado", example = "VENDEDOR")
    private Rol rol;

    @Schema(description = "Turno del empleado", example = "MANANA")
    private Turno turno;

    @Schema(description = "Fecha de contratacion", example = "2026-01-15")
    private LocalDate fechaContratacion;

    @Schema(description = "Indica si el empleado esta activo", example = "true")
    private boolean activo;

    @Schema(description = "ID del usuario asociado", example = "1")
    private Long usuarioId;
}
