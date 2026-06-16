package com.mymarket.ms_reportes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Reporte", description = "Representa un reporte generado en el sistema")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "Identificador unico", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Tipo de reporte", example = "VENTAS")
    private String tipo;

    @Schema(description = "Monto total del reporte", example = "5000000.0")
    private Double montoTotal;

    @Schema(description = "Fecha y hora de generacion", example = "2026-06-09T10:00:00")
    private LocalDateTime generadoEn;

    @Schema(description = "Inicio del periodo del reporte", example = "2026-06-01T00:00:00")
    private LocalDateTime periodoInicio;

    @Schema(description = "Fin del periodo del reporte", example = "2026-06-09T23:59:59")
    private LocalDateTime periodoFin;

    @Schema(description = "Usuario que genero el reporte", example = "admin")
    private String generadoPor;

    @Schema(description = "ID de la sucursal", example = "1")
    private Long sucursalId;

    @Schema(description = "Cantidad de registros incluidos", example = "150")
    private Integer cantidadRegistros;
}
