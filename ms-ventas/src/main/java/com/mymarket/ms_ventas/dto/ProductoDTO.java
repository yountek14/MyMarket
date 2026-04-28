package com.mymarket.ms_ventas.dto;

import lombok.Data;

@Data
public class ProductoDTO {
    private Long id;
    private String nombreProducto;
    private String categoria;
    private Double precioBase;
}