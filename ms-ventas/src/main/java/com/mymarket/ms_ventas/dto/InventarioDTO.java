package com.mymarket.ms_ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de inventario para comunicacion con ms-inventario")
public class InventarioDTO {
    private Long id;
    private Long productoId;
    private Integer stockActual;
    private String estado;
    private Boolean activo;
}