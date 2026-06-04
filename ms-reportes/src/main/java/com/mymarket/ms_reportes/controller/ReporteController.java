package com.mymarket.ms_reportes.controller;

import com.mymarket.ms_reportes.model.Reporte;
import com.mymarket.ms_reportes.service.ReporteService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reportes") // La URL base
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping // Escucha peticiones GET
    public List<Reporte> listar() {
        return reporteService.obtenerTodos();
    }

    @PostMapping // Escucha peticiones POST para crear un reporte
    public Reporte guardar(@RequestBody Reporte reporte) {
        return reporteService.crearReporte(reporte);
    }
}