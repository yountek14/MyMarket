package com.mymarket.ms_reportes.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
public class Reporte{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private Double montoTotal;
    private LocalDateTime generadoEn;
    // ... demás campos

    // 1. CONSTRUCTOR VACÍO: Indispensable para JPA
    public Reporte() {
    }

    // 2. CONSTRUCTOR CON CAMPOS: Para tu comodidad (sin el ID)
    public Reporte(String tipo, Double montoTotal, LocalDateTime generadoEn) {
        this.tipo = tipo;
        this.montoTotal = montoTotal;
        this.generadoEn = generadoEn;
    }

    // 3. GETTERS Y SETTERS: Para que Spring y JPA puedan trabajar
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Double getMontoTotal() { return montoTotal; }
    public void setMontoTotal(Double montoTotal) { this.montoTotal = montoTotal; }

    public LocalDateTime getGeneradoEn() { return generadoEn; }
    public void setGeneradoEn(LocalDateTime generadoEn) { this.generadoEn = generadoEn; }
}