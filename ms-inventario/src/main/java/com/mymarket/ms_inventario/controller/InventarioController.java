package com.mymarket.ms_inventario.controller;

import com.mymarket.ms_inventario.model.EstadoInventario;
import com.mymarket.ms_inventario.model.InventarioModel;
import com.mymarket.ms_inventario.service.InventarioService;

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
@RequestMapping("/api/v1/inventario")
@Tag(name = "Inventario", description = "Operaciones relacionadas con la gestion de inventario")
public class InventarioController {

    private final InventarioService inventarioService;

    public InventarioController(InventarioService inventarioService) {
        this.inventarioService = inventarioService;
    }

    @Operation(summary = "Obtiene todo el inventario", description = "Retorna la lista completa de registros de inventario")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public ResponseEntity<List<InventarioModel>> listarTodos() {
        return ResponseEntity.ok(inventarioService.listarTodos());
    }

    @Operation(summary = "Busca inventario por ID", description = "Retorna un registro de inventario segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Inventario encontrado"), @ApiResponse(responseCode = "404", description = "Inventario no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<InventarioModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inventarioService.buscarPorId(id));
    }

    @Operation(summary = "Crea un registro de inventario", description = "Permite agregar un nuevo registro de inventario")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Inventario creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public ResponseEntity<InventarioModel> guardar(@Valid @RequestBody InventarioModel inventario) {
        InventarioModel nuevoInventario = inventarioService.guardar(inventario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoInventario);
    }

    @Operation(summary = "Actualiza un registro de inventario", description = "Actualiza los datos de un registro de inventario existente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Inventario actualizado"), @ApiResponse(responseCode = "404", description = "Inventario no encontrado")})
    @PutMapping("/{id}")
    public ResponseEntity<InventarioModel> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioModel inventarioActualizado) {

        InventarioModel inventario = inventarioService.actualizar(id, inventarioActualizado);
        return ResponseEntity.ok(inventario);
    }

    @Operation(summary = "Elimina inventario (logico)", description = "Realiza una eliminacion logica de un registro de inventario")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Inventario eliminado"), @ApiResponse(responseCode = "404", description = "Inventario no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        inventarioService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Registra entrada de stock", description = "Incrementa la cantidad de stock de un producto")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Entrada registrada"), @ApiResponse(responseCode = "404", description = "Inventario no encontrado")})
    @PutMapping("/{id}/entrada")
    public ResponseEntity<InventarioModel> registrarEntrada(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {

        return ResponseEntity.ok(inventarioService.registrarEntrada(id, cantidad));
    }

    @Operation(summary = "Registra salida de stock", description = "Decrementa la cantidad de stock de un producto")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Salida registrada"), @ApiResponse(responseCode = "404", description = "Inventario no encontrado")})
    @PutMapping("/{id}/salida")
    public ResponseEntity<InventarioModel> registrarSalida(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {

        return ResponseEntity.ok(inventarioService.registrarSalida(id, cantidad));
    }

    @Operation(summary = "Registra merma", description = "Registra una perdida o merma de stock")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Merma registrada"), @ApiResponse(responseCode = "404", description = "Inventario no encontrado")})
    @PutMapping("/{id}/merma")
    public ResponseEntity<InventarioModel> registrarMerma(
            @PathVariable Long id,
            @RequestParam Integer cantidad) {

        return ResponseEntity.ok(inventarioService.registrarMerma(id, cantidad));
    }

    @Operation(summary = "Busca por producto", description = "Retorna registros de inventario asociados a un producto")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<InventarioModel>> buscarPorProductoId(@PathVariable Long productoId) {
        return ResponseEntity.ok(inventarioService.buscarPorProductoId(productoId));
    }

    @Operation(summary = "Busca por lote", description = "Retorna registros de inventario filtrados por numero de lote")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/lote/{lote}")
    public ResponseEntity<List<InventarioModel>> buscarPorLote(@PathVariable String lote) {
        return ResponseEntity.ok(inventarioService.buscarPorLote(lote));
    }

    @Operation(summary = "Busca por estado", description = "Filtra registros de inventario segun su estado")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<InventarioModel>> buscarPorEstado(@PathVariable EstadoInventario estado) {
        return ResponseEntity.ok(inventarioService.buscarPorEstado(estado));
    }

    @Operation(summary = "Busca por activo", description = "Filtra registros segun si estan activos o eliminados")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/activo/{activo}")
    public ResponseEntity<List<InventarioModel>> buscarPorActivo(@PathVariable Boolean activo) {
        return ResponseEntity.ok(inventarioService.buscarPorActivo(activo));
    }

    @Operation(summary = "Busca productos vencidos", description = "Retorna los registros de inventario vencidos")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/vencidos")
    public ResponseEntity<List<InventarioModel>> buscarVencidos() {
        return ResponseEntity.ok(inventarioService.buscarVencidos());
    }

    @Operation(summary = "Busca por vencer", description = "Retorna registros proximos a vencer segun un rango de dias")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/por-vencer")
    public ResponseEntity<List<InventarioModel>> buscarPorVencer(
            @RequestParam(required = false) Integer dias) {

        return ResponseEntity.ok(inventarioService.buscarPorVencer(dias));
    }

    @Operation(summary = "Busca stock bajo", description = "Retorna registros con stock por debajo de un limite")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/stock-bajo")
    public ResponseEntity<List<InventarioModel>> buscarStockBajo(
            @RequestParam Integer stockLimite) {

        return ResponseEntity.ok(inventarioService.buscarStockBajo(stockLimite));
    }
}
