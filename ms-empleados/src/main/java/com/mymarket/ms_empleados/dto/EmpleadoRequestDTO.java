package com.mymarket.ms_empleados.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.mymarket.ms_empleados.model.Rol;
import com.mymarket.ms_empleados.model.Turno;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EmpleadoRequest", description = "DTO para crear o actualizar un empleado")
public class EmpleadoRequestDTO {
    @NotBlank
    @Schema(description = "Nombre del empleado", example = "Juan")
    private String nombre;

    @NotBlank
    @Schema(description = "Apellido del empleado", example = "Perez")
    private String apellido;

    @NotBlank
    @Schema(description = "RUT del empleado", example = "12.345.678-9")
    private String rut;

    @NotBlank
    @Schema(description = "Telefono de contacto", example = "+56 9 1234 5678")
    private String telefono;

    @NotNull
    @Schema(description = "Rol del empleado", example = "VENDEDOR")
    private Rol rol;

    @NotNull
    @Schema(description = "Turno del empleado", example = "MANANA")
    private Turno turno;

    @NotNull
    @Schema(description = "Fecha de contratacion", example = "2026-01-15")
    private LocalDate fechaContratacion;

    @NotNull
    @Schema(description = "ID del usuario asociado", example = "1")
    private Long usuarioId;
}
