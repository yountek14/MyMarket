package com.mymarket.ms_pedidos.model;


import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "Pedido", description = "Representa un pedido realizado a un proveedor")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Fecha en que se realizo el pedido", example = "2026-06-01")
    private LocalDate fechaPedido;

    @Schema(description = "Estado del pedido", example = "PENDIENTE")
    private String estado;

    @Schema(description = "Monto total del pedido", example = "150000.0")
    private Double total;

    @Schema(description = "ID del proveedor asociado", example = "1")
    private Long proveedorId;

    @Schema(description = "ID del usuario que creo el pedido", example = "1")
    private Long usuarioId;

    @Schema(description = "ID del empleado que gestiono el pedido", example = "1")
    private Long empleadoId;

    @Schema(description = "Notas u observaciones del pedido", example = "Urgente")
    private String observacion;
}
