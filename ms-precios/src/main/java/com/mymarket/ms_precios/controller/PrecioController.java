package com.mymarket.ms_precios.controller;

import com.mymarket.ms_precios.dto.PrecioRequestDTO;
import com.mymarket.ms_precios.dto.PrecioResponseDTO;
import com.mymarket.ms_precios.model.Temporada;
import com.mymarket.ms_precios.service.PrecioService;

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
@RequestMapping("/api/precios")
@Tag(name = "Precios", description = "Operaciones relacionadas con la gestion de precios")
public class PrecioController {

    private final PrecioService precioService;

    public PrecioController(PrecioService precioService) {
        this.precioService = precioService;
    }

    @Operation(summary = "Obtiene todos los precios", description = "Retorna la lista completa de precios registrados")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public ResponseEntity<List<PrecioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(precioService.listarTodos());
    }

    @Operation(summary = "Obtiene precios activos", description = "Retorna la lista de precios activos")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/activos")
    public ResponseEntity<List<PrecioResponseDTO>> listarActivos() {
        return ResponseEntity.ok(precioService.listarActivos());
    }

    @Operation(summary = "Busca precios por producto", description = "Retorna los precios asociados a un producto")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<PrecioResponseDTO>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(precioService.listarPorProducto(productoId));
    }

    @Operation(summary = "Obtiene precio actual", description = "Retorna el precio actual de un producto")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa"), @ApiResponse(responseCode = "404", description = "Producto sin precio")})
    @GetMapping("/producto/{productoId}/actual")
    public ResponseEntity<PrecioResponseDTO> precioActual(@PathVariable Long productoId) {
        return ResponseEntity.ok(precioService.buscarPrecioActualDeProducto(productoId));
    }

    @Operation(summary = "Busca precios por temporada", description = "Filtra precios segun la temporada")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/temporada/{temporada}")
    public ResponseEntity<List<PrecioResponseDTO>> listarPorTemporada(@PathVariable Temporada temporada) {
        return ResponseEntity.ok(precioService.listarPorTemporada(temporada));
    }

    @Operation(summary = "Busca precio por ID", description = "Retorna un precio especifico segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Precio encontrado"), @ApiResponse(responseCode = "404", description = "Precio no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(precioService.buscarPorId(id));
    }

    @Operation(summary = "Crea un precio", description = "Permite registrar un nuevo precio para un producto")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Precio creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public ResponseEntity<PrecioResponseDTO> crear(@Valid @RequestBody PrecioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(precioService.crear(dto));
    }

    @Operation(summary = "Actualiza un precio", description = "Actualiza los datos de un precio existente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Precio actualizado"), @ApiResponse(responseCode = "404", description = "Precio no encontrado")})
    @PutMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody PrecioRequestDTO dto) {
        return ResponseEntity.ok(precioService.actualizar(id, dto));
    }

    @Operation(summary = "Desactiva un precio", description = "Realiza una desactivacion logica de un precio")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Precio desactivado"), @ApiResponse(responseCode = "404", description = "Precio no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        precioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
