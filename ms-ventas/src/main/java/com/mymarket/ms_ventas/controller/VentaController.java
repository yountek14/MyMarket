package com.mymarket.ms_ventas.controller;

import com.mymarket.ms_ventas.model.EstadoVenta;
import com.mymarket.ms_ventas.model.VentaModel;
import com.mymarket.ms_ventas.service.VentaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    //Listar todas
    @GetMapping
    public ResponseEntity<List<VentaModel>> listar() {
        return ResponseEntity.ok(ventaService.listarTodos());
    }

    //Buscar por ID
    @GetMapping("/{id}")
    public ResponseEntity<VentaModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    //Crear venta
    @PostMapping
    public ResponseEntity<VentaModel> crear(@RequestBody VentaModel venta) {
        return ResponseEntity.status(201).body(ventaService.registrarVenta(venta));
    }

    //Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<VentaModel> actualizar(
            @PathVariable Long id,
            @RequestBody VentaModel venta) {

        return ResponseEntity.ok(ventaService.actualizar(id, venta));
    }

    //Eliminación lógica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    //Marcar como pagada
    @PutMapping("/{id}/pagar")
    public ResponseEntity<VentaModel> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.marcarComoPagada(id));
    }

    //Anular venta
    @PutMapping("/{id}/anular")
    public ResponseEntity<VentaModel> anular(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.anularVenta(id));
    }

    //Filtros
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<VentaModel>> buscarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(ventaService.buscarPorProductoId(productoId));
    }

    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<VentaModel>> buscarPorInventario(@PathVariable Long inventarioId) {
        return ResponseEntity.ok(ventaService.buscarPorInventarioId(inventarioId));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VentaModel>> buscarPorEstado(@PathVariable EstadoVenta estado) {
        return ResponseEntity.ok(ventaService.buscarPorEstado(estado));
    }

    @GetMapping("/activo/{activo}")
    public ResponseEntity<List<VentaModel>> buscarPorActivo(@PathVariable Boolean activo) {
        return ResponseEntity.ok(ventaService.buscarPorActivo(activo));
    }

    //Buscar por rango de fechas
    @GetMapping("/fechas")
    public ResponseEntity<List<VentaModel>> buscarPorFechas(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {

        return ResponseEntity.ok(ventaService.buscarPorRangoFechas(inicio, fin));
    }
}