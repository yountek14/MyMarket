package com.mymarket.ms_pedidos.controller;

import com.mymarket.ms_pedidos.model.Pedido;
import com.mymarket.ms_pedidos.service.IPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final IPedidoService service;

    public PedidoController(IPedidoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> buscarPorEstado(@PathVariable String estado) {
        return ResponseEntity.ok(service.buscarPorEstado(estado));
    }

    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Pedido>> buscarPorProveedor(@PathVariable Long proveedorId) {
        return ResponseEntity.ok(service.buscarPorProveedor(proveedorId));
    }

    @PostMapping
    public ResponseEntity<Pedido> guardar(@RequestBody Pedido pedido) {
        Pedido guardado = service.guardar(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        service.buscarPorId(id);
        pedido.setId(id);
        return ResponseEntity.ok(service.guardar(pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}