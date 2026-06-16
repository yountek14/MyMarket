package com.mymarket.ms_ventas.controller;

import com.mymarket.ms_ventas.model.EstadoVenta;
import com.mymarket.ms_ventas.model.VentaModel;
import com.mymarket.ms_ventas.service.VentaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ventas")
@Tag(name = "Ventas", description = "Operaciones relacionadas con la gestion de ventas")
public class VentaController {

    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Operation(summary = "Obtiene todas las ventas", description = "Retorna la lista completa de ventas registradas")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public ResponseEntity<List<VentaModel>> listar() {
        return ResponseEntity.ok(ventaService.listarTodos());
    }

    @Operation(summary = "Busca venta por ID", description = "Retorna una venta especifica segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Venta encontrada"), @ApiResponse(responseCode = "404", description = "Venta no encontrada")})
    @GetMapping("/{id}")
    public ResponseEntity<VentaModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.buscarPorId(id));
    }

    @Operation(summary = "Crea una venta", description = "Permite registrar una nueva venta en el sistema")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Venta creada"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public ResponseEntity<VentaModel> crear(@RequestBody VentaModel venta) {
        return ResponseEntity.status(201).body(ventaService.registrarVenta(venta));
    }

    @Operation(summary = "Actualiza una venta", description = "Actualiza los datos de una venta existente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Venta actualizada"), @ApiResponse(responseCode = "404", description = "Venta no encontrada")})
    @PutMapping("/{id}")
    public ResponseEntity<VentaModel> actualizar(
            @PathVariable Long id,
            @RequestBody VentaModel venta) {

        return ResponseEntity.ok(ventaService.actualizar(id, venta));
    }

    @Operation(summary = "Elimina venta (logico)", description = "Realiza una eliminacion logica de una venta")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Venta eliminada"), @ApiResponse(responseCode = "404", description = "Venta no encontrada")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ventaService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Marca venta como pagada", description = "Cambia el estado de una venta a pagada")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Venta pagada"), @ApiResponse(responseCode = "404", description = "Venta no encontrada")})
    @PutMapping("/{id}/pagar")
    public ResponseEntity<VentaModel> pagar(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.marcarComoPagada(id));
    }

    @Operation(summary = "Anula una venta", description = "Cambia el estado de una venta a anulada")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Venta anulada"), @ApiResponse(responseCode = "404", description = "Venta no encontrada")})
    @PutMapping("/{id}/anular")
    public ResponseEntity<VentaModel> anular(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.anularVenta(id));
    }

    @Operation(summary = "Busca ventas por producto", description = "Retorna ventas asociadas a un producto")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<VentaModel>> buscarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(ventaService.buscarPorProductoId(productoId));
    }

    @Operation(summary = "Busca ventas por inventario", description = "Retorna ventas asociadas a un registro de inventario")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/inventario/{inventarioId}")
    public ResponseEntity<List<VentaModel>> buscarPorInventario(@PathVariable Long inventarioId) {
        return ResponseEntity.ok(ventaService.buscarPorInventarioId(inventarioId));
    }

    @Operation(summary = "Busca ventas por estado", description = "Filtra ventas segun su estado")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<VentaModel>> buscarPorEstado(@PathVariable EstadoVenta estado) {
        return ResponseEntity.ok(ventaService.buscarPorEstado(estado));
    }

    @Operation(summary = "Busca ventas por activo", description = "Filtra ventas segun si estan activas o eliminadas")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/activo/{activo}")
    public ResponseEntity<List<VentaModel>> buscarPorActivo(@PathVariable Boolean activo) {
        return ResponseEntity.ok(ventaService.buscarPorActivo(activo));
    }

    @Operation(summary = "Busca ventas por rango de fechas", description = "Retorna ventas dentro de un rango de fechas")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/fechas")
    public ResponseEntity<List<VentaModel>> buscarPorFechas(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fin) {

        return ResponseEntity.ok(ventaService.buscarPorRangoFechas(inicio, fin));
    }
}
