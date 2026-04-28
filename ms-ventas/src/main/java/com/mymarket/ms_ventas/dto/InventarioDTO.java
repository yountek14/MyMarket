package com.mymarket.ms_ventas.dto;

import lombok.Data;

@Data
public class InventarioDTO {
    private Long id;
    private Long productoId;
    private Integer stockActual;
    private String estado;
    private Boolean activo;
}