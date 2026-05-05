package com.mymarket.ms_proveedores.model;
//package com.mymarket.proveedores.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "proveedores")
@Data //Data genera getter, setters y toString de manera automatica
@NoArgsConstructor//Esto genera el constructor Proveedor () {}
@AllArgsConstructor//Genera el constructor Proveedor(id,rut,nombre, etc...)
public class Proveedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rut;
    private String nombre;
    private String direccion;
    private String telefono;
    private String email;
    private String rubro;
}
