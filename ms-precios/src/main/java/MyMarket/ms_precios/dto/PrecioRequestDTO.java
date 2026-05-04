package MyMarket.ms_precios.dto;

import MyMarket.ms_precios.model.Temporada;
import MyMarket.ms_precios.model.TipoDescuento;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor @AllArgsConstructor
public class PrecioRequestDTO{
    @NotNull
    private Long productoId;

    @NotNull
    private Double precioBase;

    private TipoDescuento tipoDescuento;
    private Double valorDescuento;

    private Temporada temporada;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}
