package com.mymarket.ms_precios.dto;

import com.mymarket.ms_precios.model.Temporada;
import com.mymarket.ms_precios.model.TipoDescuento;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "PrecioResponse", description = "DTO de respuesta con datos del precio")
public class PrecioResponseDTO {
    @Schema(title = "Identificador unico", example = "1")
    private Long id;

    @Schema(description = "ID del producto asociado", example = "1")
    private Long productoId;

    @Schema(description = "Precio base del producto", example = "890000.0")
    private Double precioBase;

    @Schema(description = "Tipo de descuento", example = "PORCENTAJE")
    private TipoDescuento tipoDescuento;

    @Schema(description = "Valor del descuento", example = "10.0")
    private Double valorDescuento;

    @Schema(description = "Precio final calculado", example = "801000.0")
    private Double precioFinal;

    @Schema(description = "Temporada asociada", example = "VERANO")
    private Temporada temporada;

    @Schema(description = "Fecha de inicio de vigencia", example = "2026-01-01")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin de vigencia", example = "2026-03-31")
    private LocalDate fechaFin;

    @Schema(description = "Indica si el precio esta activo", example = "true")
    private boolean activo;
}
