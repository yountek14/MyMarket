package com.mymarket.ms_empleados.model;

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
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    @Column(unique = true)
    private String rut;

    @NotBlank
    private String telefono;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Turno turno;

    @NotNull
    private LocalDate fechaContratacion;

    private boolean activo = true;

    @NotNull
    private Long usuarioId;

}
