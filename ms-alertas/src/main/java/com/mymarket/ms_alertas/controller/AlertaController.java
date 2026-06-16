package com.mymarket.ms_alertas.controller;

import com.mymarket.ms_alertas.model.AlertaModel;
import com.mymarket.ms_alertas.model.EstadoAlerta;
import com.mymarket.ms_alertas.model.TipoAlerta;
import com.mymarket.ms_alertas.service.AlertaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alertas")
@Tag(name = "Alertas", description = "Operaciones relacionadas con la gestion de alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @Operation(summary = "Obtiene todas las alertas", description = "Retorna la lista completa de alertas registradas")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public ResponseEntity<List<AlertaModel>> listarTodas() {
        return ResponseEntity.ok(alertaService.listarTodas());
    }

    @Operation(summary = "Busca alerta por ID", description = "Retorna una alerta especifica segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Alerta encontrada"), @ApiResponse(responseCode = "404", description = "Alerta no encontrada")})
    @GetMapping("/{id}")
    public ResponseEntity<AlertaModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.buscarPorId(id));
    }

    @Operation(summary = "Crea una alerta manual", description = "Permite registrar una alerta de forma manual")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Alerta creada"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public ResponseEntity<AlertaModel> crearAlertaManual(@Valid @RequestBody AlertaModel alerta) {
        AlertaModel nuevaAlerta = alertaService.crearAlertaManual(alerta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAlerta);
    }

    @Operation(summary = "Resuelve una alerta", description = "Marca una alerta como resuelta segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Alerta resuelta"), @ApiResponse(responseCode = "404", description = "Alerta no encontrada")})
    @PutMapping("/{id}/resolver")
    public ResponseEntity<AlertaModel> resolverAlerta(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.resolverAlerta(id));
    }

    @Operation(summary = "Elimina una alerta (logico)", description = "Realiza una eliminacion logica de una alerta")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Alerta eliminada"), @ApiResponse(responseCode = "404", description = "Alerta no encontrada")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        alertaService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca alertas por producto", description = "Retorna alertas asociadas a un producto especifico")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<AlertaModel>> buscarPorProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(alertaService.buscarPorProductoId(productoId));
    }

    @Operation(summary = "Busca alertas por inventario", description = "Retorna alertas asociadas a un registro de inventario")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<AlertaModel>> buscarPorInventarioId(@PathVariable Long inventarioId) {
        return ResponseEntity.ok(alertaService.buscarPorInventarioId(inventarioId));
    }

    @Operation(summary = "Busca alertas por tipo", description = "Filtra alertas segun su tipo (STOCK o VENCIMIENTO)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/tipo/{tipoAlerta}")
    public ResponseEntity<List<AlertaModel>> buscarPorTipo(@PathVariable TipoAlerta tipoAlerta) {
        return ResponseEntity.ok(alertaService.buscarPorTipo(tipoAlerta));
    }

    @Operation(summary = "Busca alertas por estado", description = "Filtra alertas segun su estado (ACTIVA o RESUELTA)")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/estado/{estadoAlerta}")
    public ResponseEntity<List<AlertaModel>> buscarPorEstado(@PathVariable EstadoAlerta estadoAlerta) {
        return ResponseEntity.ok(alertaService.buscarPorEstado(estadoAlerta));
    }

    @Operation(summary = "Busca alertas por activo", description = "Filtra alertas segun si estan activas o eliminadas")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/activo/{activo}")
    public ResponseEntity<List<AlertaModel>> buscarPorActivo(@PathVariable Boolean activo) {
        return ResponseEntity.ok(alertaService.buscarPorActivo(activo));
    }

    @Operation(summary = "Genera alerta de stock", description = "Genera automaticamente una alerta de stock bajo para un inventario")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Alerta generada")})
    @PostMapping("/generar-stock/{inventarioId}")
    public ResponseEntity<AlertaModel> generarAlertaStock(@PathVariable Long inventarioId) {
        AlertaModel alerta = alertaService.generarAlertaStock(inventarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(alerta);
    }

    @Operation(summary = "Genera alerta de vencimiento", description = "Genera automaticamente una alerta de vencimiento para un inventario")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Alerta generada")})
    @PostMapping("/generar-vencimiento/{inventarioId}")
    public ResponseEntity<AlertaModel> generarAlertaVencimiento(@PathVariable Long inventarioId) {
        AlertaModel alerta = alertaService.generarAlertaVencimiento(inventarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(alerta);
    }
}
