package com.mymarket.ms_ventas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO de producto para comunicacion con ms-productos")
public class ProductoDTO {
    private Long id;
    private String nombreProducto;
    private String categoria;
    private Double precioBase;
}