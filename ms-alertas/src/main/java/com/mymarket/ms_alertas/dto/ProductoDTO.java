package com.mymarket.ms_alertas.dto;

import lombok.Data;

@Data
public class ProductoDTO {

    private Long id;
    private String nombreProducto;
    private String categoria;
    private String unidadMedida;
    private Double precioBase;
    private Boolean activo;
    private String descripcionProducto;
}