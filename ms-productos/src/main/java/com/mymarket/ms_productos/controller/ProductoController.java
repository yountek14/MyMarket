package com.mymarket.ms_productos.controller;

import com.mymarket.ms_productos.model.ProductoModel;
import com.mymarket.ms_productos.model.UnidadMedida;
import com.mymarket.ms_productos.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // Listar todos los productos
    @GetMapping
    public ResponseEntity<List<ProductoModel>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    // Buscar producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProductoModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    // Crear producto
    @PostMapping
    public ResponseEntity<ProductoModel> guardar(@Valid @RequestBody ProductoModel producto) {
        ProductoModel nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<ProductoModel> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody ProductoModel productoActualizado) {
        ProductoModel producto = productoService.actualizar(id, productoActualizado);
        return ResponseEntity.ok(producto);
    }

    // Eliminación lógica
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        productoService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    // Buscar por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoModel>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
    }

    // Buscar por estado activo
    @GetMapping("/activo/{activo}")
    public ResponseEntity<List<ProductoModel>> buscarPorActivo(@PathVariable Boolean activo) {
        return ResponseEntity.ok(productoService.buscarPorActivo(activo));
    }

    // Buscar por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoModel>> buscarPorNombre(@RequestParam String nombreProducto) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombreProducto));
    }

    // Buscar por unidad de medida
    @GetMapping("/unidad/{unidadMedida}")
    public ResponseEntity<List<ProductoModel>> buscarPorUnidadMedida(@PathVariable UnidadMedida unidadMedida) {
        return ResponseEntity.ok(productoService.buscarPorUnidadMedida(unidadMedida));
    }
}