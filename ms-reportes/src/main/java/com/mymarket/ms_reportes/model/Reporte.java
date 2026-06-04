package com.mymarket.ms_reportes.model;

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
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private Double montoTotal;
    private LocalDateTime generadoEn;
    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFin;
    private String generadoPor;
    private Long sucursalId;
    private Integer cantidadRegistros;
}
