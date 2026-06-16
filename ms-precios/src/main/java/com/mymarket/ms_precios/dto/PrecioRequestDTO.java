package com.mymarket.ms_precios.dto;

import com.mymarket.ms_precios.model.Temporada;
import com.mymarket.ms_precios.model.TipoDescuento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
@Schema(name = "PrecioRequest", description = "DTO para crear o actualizar un precio")
public class PrecioRequestDTO{
    @NotNull
    @Schema(description = "ID del producto asociado", example = "1")
    private Long productoId;

    @NotNull
    @Schema(description = "Precio base del producto", example = "890000.0", minimum = "0")
    private Double precioBase;

    @Schema(description = "Tipo de descuento", example = "PORCENTAJE")
    private TipoDescuento tipoDescuento;

    @Schema(description = "Valor del descuento", example = "10.0")
    private Double valorDescuento;

    @Schema(description = "Temporada asociada", example = "VERANO")
    private Temporada temporada;

    @Schema(description = "Fecha de inicio de vigencia", example = "2026-01-01")
    private LocalDate fechaInicio;

    @Schema(description = "Fecha de fin de vigencia", example = "2026-03-31")
    private LocalDate fechaFin;
}
