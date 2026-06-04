package com.mymarket.ms_proveedores.controller;

import com.mymarket.ms_proveedores.model.Proveedor;
import com.mymarket.ms_proveedores.service.IProveedorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final IProveedorService service;

    public ProveedorController(IProveedorService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Proveedor>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/rut/{rut}")
    public ResponseEntity<Proveedor> buscarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(service.buscarPorRut(rut));
    }

    @PostMapping
    public ResponseEntity<Proveedor> guardar(@RequestBody Proveedor proveedor){
        Proveedor guardado = service.guardar(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        service.buscarPorId(id);
        proveedor.setId(id);
        return ResponseEntity.ok(service.guardar(proveedor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}

