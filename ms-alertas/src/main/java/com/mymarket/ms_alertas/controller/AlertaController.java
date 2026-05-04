package com.mymarket.ms_alertas.controller;

import com.mymarket.ms_alertas.model.AlertaModel;
import com.mymarket.ms_alertas.model.EstadoAlerta;
import com.mymarket.ms_alertas.model.TipoAlerta;
import com.mymarket.ms_alertas.service.AlertaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/alertas")
public class AlertaController {

    private final AlertaService alertaService;

    public AlertaController(AlertaService alertaService) {
        this.alertaService = alertaService;
    }

    @GetMapping
    public ResponseEntity<List<AlertaModel>> listarTodas() {
        return ResponseEntity.ok(alertaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlertaModel> crearAlertaManual(@Valid @RequestBody AlertaModel alerta) {
        AlertaModel nuevaAlerta = alertaService.crearAlertaManual(alerta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaAlerta);
    }

    @PutMapping("/{id}/resolver")
    public ResponseEntity<AlertaModel> resolverAlerta(@PathVariable Long id) {
        return ResponseEntity.ok(alertaService.resolverAlerta(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        alertaService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<AlertaModel>> buscarPorProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(alertaService.buscarPorProductoId(productoId));
    }

    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<AlertaModel>> buscarPorInventarioId(@PathVariable Long inventarioId) {
        return ResponseEntity.ok(alertaService.buscarPorInventarioId(inventarioId));
    }

    @GetMapping("/tipo/{tipoAlerta}")
    public ResponseEntity<List<AlertaModel>> buscarPorTipo(@PathVariable TipoAlerta tipoAlerta) {
        return ResponseEntity.ok(alertaService.buscarPorTipo(tipoAlerta));
    }

    @GetMapping("/estado/{estadoAlerta}")
    public ResponseEntity<List<AlertaModel>> buscarPorEstado(@PathVariable EstadoAlerta estadoAlerta) {
        return ResponseEntity.ok(alertaService.buscarPorEstado(estadoAlerta));
    }

    @GetMapping("/activo/{activo}")
    public ResponseEntity<List<AlertaModel>> buscarPorActivo(@PathVariable Boolean activo) {
        return ResponseEntity.ok(alertaService.buscarPorActivo(activo));
    }

    @PostMapping("/generar-stock/{inventarioId}")
    public ResponseEntity<AlertaModel> generarAlertaStock(@PathVariable Long inventarioId) {
        AlertaModel alerta = alertaService.generarAlertaStock(inventarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(alerta);
    }

    @PostMapping("/generar-vencimiento/{inventarioId}")
    public ResponseEntity<AlertaModel> generarAlertaVencimiento(@PathVariable Long inventarioId) {
        AlertaModel alerta = alertaService.generarAlertaVencimiento(inventarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(alerta);
    }
}
