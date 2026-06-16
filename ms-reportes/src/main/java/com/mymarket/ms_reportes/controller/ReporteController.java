package com.mymarket.ms_reportes.controller;

import com.mymarket.ms_reportes.model.Reporte;
import com.mymarket.ms_reportes.service.ReporteService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reportes", description = "Operaciones relacionadas con la generacion de reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @Operation(summary = "Obtiene todos los reportes", description = "Retorna la lista completa de reportes generados")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public List<Reporte> listar() {
        return reporteService.obtenerTodos();
    }

    @Operation(summary = "Crea un reporte", description = "Permite generar un nuevo reporte")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Reporte creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public Reporte guardar(@RequestBody Reporte reporte) {
        return reporteService.crearReporte(reporte);
    }
}
