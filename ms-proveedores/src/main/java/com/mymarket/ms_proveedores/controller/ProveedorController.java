package com.mymarket.ms_proveedores.controller;

import com.mymarket.ms_proveedores.model.Proveedor;
import com.mymarket.ms_proveedores.service.IProveedorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@Tag(name = "Proveedores", description = "Operaciones relacionadas con la gestion de proveedores")
public class ProveedorController {

    private final IProveedorService service;

    public ProveedorController(IProveedorService service) {
        this.service = service;
    }

    @Operation(summary = "Obtiene todos los proveedores", description = "Retorna la lista completa de proveedores registrados")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public ResponseEntity<List<Proveedor>> listarTodos(){
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Busca proveedor por ID", description = "Retorna un proveedor especifico segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Proveedor encontrado"), @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @Operation(summary = "Busca proveedor por RUT", description = "Retorna un proveedor filtrado por su RUT")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Proveedor encontrado"), @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")})
    @GetMapping("/rut/{rut}")
    public ResponseEntity<Proveedor> buscarPorRut(@PathVariable String rut) {
        return ResponseEntity.ok(service.buscarPorRut(rut));
    }

    @Operation(summary = "Crea un proveedor", description = "Permite registrar un nuevo proveedor")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Proveedor creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public ResponseEntity<Proveedor> guardar(@RequestBody Proveedor proveedor){
        Proveedor guardado = service.guardar(proveedor);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @Operation(summary = "Actualiza un proveedor", description = "Actualiza los datos de un proveedor existente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Proveedor actualizado"), @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")})
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Long id, @RequestBody Proveedor proveedor) {
        service.buscarPorId(id);
        proveedor.setId(id);
        return ResponseEntity.ok(service.guardar(proveedor));
    }

    @Operation(summary = "Elimina un proveedor", description = "Elimina un proveedor del sistema")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Proveedor eliminado"), @ApiResponse(responseCode = "404", description = "Proveedor no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
