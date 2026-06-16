package com.mymarket.ms_usuario.model;

import com.mymarket.ms_usuario.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data @NoArgsConstructor @AllArgsConstructor
@Schema(name = "Usuario", description = "Representa un usuario del sistema")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank
    @Schema(description = "Nombre completo del usuario", example = "Juan Perez")
    private String nombre;

    @Email @NotBlank
    @Column(unique = true)
    @Schema(description = "Correo electronico del usuario", example = "juan.perez@correo.cl")
    private String email;

    @NotBlank
    @Schema(description = "Contrasena del usuario", example = "********")
    private String password;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Rol del usuario en el sistema", example = "ADMIN")
    private Rol rol;

    @Schema(description = "Indica si el usuario esta activo", example = "true", defaultValue = "true")
    private boolean activo = true;
}