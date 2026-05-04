package MyMarket.ms_precios.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "precios")
@Data @NoArgsConstructor @AllArgsConstructor
public class Precio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long productoId;

    @NotNull
    private Double precioBase;

    @Enumerated(EnumType.STRING)
    private TipoDescuento tipoDescuento;

    private Double valorDescuento;

    @Enumerated(EnumType.STRING)
    private Temporada temporada;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    private boolean activo = true;
}
