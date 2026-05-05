package com.mymarket.ms_precios.dto;

import com.mymarket.ms_precios.model.Temporada;
import com.mymarket.ms_precios.model.TipoDescuento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrecioResponseDTO {
    private Long id;
    private Long productoId;
    private Double precioBase;
    private TipoDescuento tipoDescuento;
    private Double valorDescuento;
    private Double precioFinal;
    private Temporada temporada;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private boolean activo;
}
