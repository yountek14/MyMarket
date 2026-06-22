package com.mymarket.ms_alertas.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "DTO de inventario para comunicacion con ms-inventario")
public class InventarioDTO {

    private Long id;
    private Long productoId;
    private String lote;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private Integer merma;
    private LocalDate fechaIngreso;
    private LocalDate fechaVencimiento;
    private String estado;
    private Boolean activo;
}
