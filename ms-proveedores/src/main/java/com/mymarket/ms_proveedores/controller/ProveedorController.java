package com.mymarket.ms_proveedores.controller;

import com.mymarket.ms_proveedores.model.Proveedor;
import com.mymarket.ms_proveedores.service.IProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private IProveedorService service;

    // GET lista todos
    @GetMapping
    public ResponseEntity<List<Proveedor>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }

    // GET busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> buscarPorId(@PathVariable Long id) {
        Proveedor p = service.buscarPorId(id);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }
    // GET busca por RUT
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Proveedor> buscarPorRut(@PathVariable String rut) {
        Proveedor p = service.buscarPorRut(rut);
        if (p == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(p);
    }
    //POST crea un nuevo proveedor
    @PostMapping
    public ResponseEntity<Proveedor> guardar(@RequestBody Proveedor proveedor){
        Proveedor guardado = service.guardar(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    // PUT actualiza un proveedor existente
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        Proveedor existente = service.buscarPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();
        proveedor.setId(id);
        return ResponseEntity.ok(service.guardar(proveedor));
    }

    // DELETE elimina por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Proveedor existente = service.buscarPorId(id);
        if (existente == null) return ResponseEntity.notFound().build();
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

