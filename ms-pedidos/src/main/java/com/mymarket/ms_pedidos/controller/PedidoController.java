package com.mymarket.ms_pedidos.controller;

import com.mymarket.ms_pedidos.model.Pedido;
import com.mymarket.ms_pedidos.service.IPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private IPedidoService service;

    // GET /api/pedidos    lista todos los pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET /api/pedidos/{id}    busca un pedido por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPorId(@PathVariable Long id) {
        Pedido p = service.buscarPorId(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }

    // GET /api/pedidos/estado/{estado}    filtra por estado (PENDIENTE, ENTREGADO, etc)
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<Pedido>> buscarPorEstado(@PathVariable String estado) {
        List<Pedido> lista = service.buscarPorEstado(estado);
        return ResponseEntity.ok(lista);
    }

    // GET /api/pedidos/proveedor/{proveedorId}    filtra por proveedor
    @GetMapping("/proveedor/{proveedorId}")
    public ResponseEntity<List<Pedido>> buscarPorProveedor(@PathVariable Long proveedorId) {
        List<Pedido> lista = service.buscarPorProveedor(proveedorId);
        return ResponseEntity.ok(lista);
    }

    // POST /api/pedidos    crea un nuevo pedido
    @PostMapping
    public ResponseEntity<Pedido> guardar(@RequestBody Pedido pedido) {
        Pedido guardado = service.guardar(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // PUT /api/pedidos/{id}    actualiza un pedido existente
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizar(@PathVariable Long id, @RequestBody Pedido pedido) {
        Pedido existente = service.buscarPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();
        pedido.setId(id);
        return ResponseEntity.ok(service.guardar(pedido));
    }

    // DELETE /api/pedidos/{id}    elimina un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Pedido existente = service.buscarPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}