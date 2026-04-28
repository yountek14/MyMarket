package com.example.ms_pedidos.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fechaPedido;   // Fecha en que se realizó el pedido
    private String estado;           // "PENDIENTE", "ENTREGADO", "CANCELADO"
    private Double total;            // Monto total del pedido
    private Long proveedorId;        // ID del proveedor (referencia a ms-proveedores)
    private Long usuarioId;          // ID del usuario que hizo el pedido
    private Long empleadoId;         // ID del empleado que gestionó el pedido
    private String observacion;      // Notas opcionales del pedido
}
