package com.mymarket.ms_productos.controller;

import com.mymarket.ms_productos.model.ProductoModel;
import com.mymarket.ms_productos.model.UnidadMedida;
import com.mymarket.ms_productos.service.ProductoService;

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
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con la gestion de productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @Operation(summary = "Obtiene todos los productos", description = "Retorna la lista completa de productos registrados")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public ResponseEntity<List<ProductoModel>> listarTodos() {
        return ResponseEntity.ok(productoService.listarTodos());
    }

    @Operation(summary = "Busca producto por ID", description = "Retorna un producto especifico segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Producto encontrado"), @ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<ProductoModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.buscarPorId(id));
    }

    @Operation(summary = "Crea un producto", description = "Permite registrar un nuevo producto en el catalogo")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Producto creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public ResponseEntity<ProductoModel> guardar(@Valid @RequestBody ProductoModel producto) {
        ProductoModel nuevoProducto = productoService.guardar(producto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
    }

    @Operation(summary = "Actualiza un producto", description = "Actualiza los datos de un producto existente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Producto actualizado"), @ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    @PutMapping("/{id}")
    public ResponseEntity<ProductoModel> actualizar(@PathVariable Long id,
                                                    @Valid @RequestBody ProductoModel productoActualizado) {
        ProductoModel producto = productoService.actualizar(id, productoActualizado);
        return ResponseEntity.ok(producto);
    }

    @Operation(summary = "Elimina producto (logico)", description = "Realiza una eliminacion logica de un producto")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Producto eliminado"), @ApiResponse(responseCode = "404", description = "Producto no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLogico(@PathVariable Long id) {
        productoService.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Busca por categoria", description = "Retorna productos filtrados por categoria")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProductoModel>> buscarPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.buscarPorCategoria(categoria));
    }

    @Operation(summary = "Busca por estado activo", description = "Filtra productos segun si estan activos o eliminados")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/activo/{activo}")
    public ResponseEntity<List<ProductoModel>> buscarPorActivo(@PathVariable Boolean activo) {
        return ResponseEntity.ok(productoService.buscarPorActivo(activo));
    }

    @Operation(summary = "Busca por nombre", description = "Retorna productos filtrados por nombre")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/buscar")
    public ResponseEntity<List<ProductoModel>> buscarPorNombre(@RequestParam String nombreProducto) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombreProducto));
    }

    @Operation(summary = "Busca por unidad de medida", description = "Filtra productos segun su unidad de medida")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/unidad/{unidadMedida}")
    public ResponseEntity<List<ProductoModel>> buscarPorUnidadMedida(@PathVariable UnidadMedida unidadMedida) {
        return ResponseEntity.ok(productoService.buscarPorUnidadMedida(unidadMedida));
    }
}
