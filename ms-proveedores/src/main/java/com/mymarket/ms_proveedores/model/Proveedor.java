package com.mymarket.ms_proveedores.model;
//package com.mymarket.proveedores.models.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Proveedor", description = "Representa un proveedor registrado en el sistema")
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "RUT del proveedor", example = "76.123.456-7")
    private String rut;

    @Schema(description = "Nombre del proveedor", example = "Distribuidora Tecnologica Ltda.")
    private String nombre;

    @Schema(description = "Direccion del proveedor", example = "Av. Providencia 1234")
    private String direccion;

    @Schema(description = "Telefono de contacto", example = "+56 2 2123 4567")
    private String telefono;

    @Schema(description = "Correo electronico", example = "contacto@distribuidora.cl")
    private String email;

    @Schema(description = "Rubro del proveedor", example = "Tecnologia")
    private String rubro;
}
