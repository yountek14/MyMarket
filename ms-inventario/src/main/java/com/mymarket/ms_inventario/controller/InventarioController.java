package com.mymarket.ms_inventario.controller;

import com.mymarket.ms_inventario.model.EstadoInventario;
import com.mymarket.ms_inventario.model.InventarioModel;
import com.mymarket.ms_inventario.service.InventarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @GetMapping
    public ResponseEntity<List<InventarioModel>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<InventarioModel> guardar(@Valid @RequestBody InventarioModel inventario) {
        InventarioModel nuevoInventario = inventarioService.guardar(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioModel> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioModel inventarioActualizado) {

        InventarioModel inventario = inventarioService.actualizar(id, inventarioActualizado);
        return ResponseEntity.ok(inventario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        inventarioService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/entrada")
    public ResponseEntity<InventarioModel> registrarEntrada(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {

        return ResponseEntity.ok(inventarioService.registrarEntrada(id, cantidad));
    }

    @PutMapping("/{id}/salida")
    public ResponseEntity<InventarioModel> registrarSalida(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {

        return ResponseEntity.ok(inventarioService.registrarSalida(id, cantidad));
    }

    @PutMapping("/{id}/merma")
    public ResponseEntity<InventarioModel> registrarMerma(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {

        return ResponseEntity.ok(inventarioService.registrarMerma(id, cantidad));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<InventarioModel>> buscarPorProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarPorProductoId(productoId));
    }

    @GetMapping("/lote/{lote}")
    public ResponseEntity<List<InventarioModel>> buscarPorLote(@PathVariable String lote) {
        return ResponseEntity.ok(inventarioService.buscarPorLote(lote));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<InventarioModel>> buscarPorEstado(@PathVariable EstadoInventario estado) {
        return ResponseEntity.ok(inventarioService.buscarPorEstado(estado));
    }

    @GetMapping("/activo/{activo}")
    public ResponseEntity<List<InventarioModel>> buscarPorActivo(@PathVariable Boolean activo) {
        return ResponseEntity.ok(inventarioService.buscarPorActivo(activo));
    }

    @GetMapping("/vencidos")
    public ResponseEntity<List<InventarioModel>> buscarVencidos() {
        return ResponseEntity.ok(inventarioService.buscarVencidos());
    }

    @GetMapping("/por-vencer")
    public ResponseEntity<List<InventarioModel>> buscarPorVencer(
            @RequestParam(required = false) Integer dias) {

        return ResponseEntity.ok(inventarioService.buscarPorVencer(dias));
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<List<InventarioModel>> buscarStockBajo(
            @RequestParam Integer stockLimite) {

        return ResponseEntity.ok(inventarioService.buscarStockBajo(stockLimite));
    }
}