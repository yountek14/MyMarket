package com.mymarket.ms_pedidos.controller;

import com.mymarket.ms_pedidos.model.Pedido;
import com.mymarket.ms_pedidos.service.IPedidoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Operaciones relacionadas con la gestion de pedidos")
public class PedidoController {

    private final IPedidoService service;

    public PedidoController(IPedidoService service) {
        this.service = service;
    }

    @Operation(summary = "Obtiene todos los pedidos", description = "Retorna la lista completa de pedidos registrados")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Busca pedido por ID", description = "Retorna un pedido especifico segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Pedido encontrado"), @ApiResponse(responseCode = "404", description = "Pedido no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Busca pedidos por estado", description = "Filtra pedidos segun su estado")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @Operation(summary = "Busca pedidos por proveedor", description = "Retorna pedidos asociados a un proveedor")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Pedido>> buscarPorProveedor(@PathVariable Long proveedorId) {
        return ResponseEntity.ok(service.buscarPorProveedor(proveedorId));
    }

    @Operation(summary = "Crea un pedido", description = "Permite registrar un nuevo pedido")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Pedido creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public ResponseEntity<Pedido> guardar(@RequestBody Pedido pedido) {
        Pedido guardado = service.guardar(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @Operation(summary = "Actualiza un pedido", description = "Actualiza los datos de un pedido existente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Pedido actualizado"), @ApiResponse(responseCode = "404", description = "Pedido no encontrado")})
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        service.buscarPorId(id);
        pedido.setId(id);
        return ResponseEntity.ok(service.guardar(pedido));
    }

    @Operation(summary = "Elimina un pedido", description = "Elimina un pedido del sistema")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Pedido eliminado"), @ApiResponse(responseCode = "404", description = "Pedido no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
