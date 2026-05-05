package com.mymarket.ms_precios.controller;

import com.mymarket.ms_precios.dto.PrecioRequestDTO;
import com.mymarket.ms_precios.dto.PrecioResponseDTO;
import com.mymarket.ms_precios.model.Temporada;
import com.mymarket.ms_precios.service.PrecioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/precios")
public class PrecioController {

    @Autowired
    private PrecioService precioService;

    @GetMapping
    public ResponseEntity<List<PrecioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(precioService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PrecioResponseDTO>> listarActivos() {
        return ResponseEntity.ok(precioService.listarActivos());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<PrecioResponseDTO>> listarPorProducto(@PathVariable Long productoId) {
        return ResponseEntity.ok(precioService.listarPorProducto(productoId));
    }

    @GetMapping("/producto/{productoId}/actual")
    public ResponseEntity<PrecioResponseDTO> precioActual(@PathVariable Long productoId) {
        return ResponseEntity.ok(precioService.buscarPrecioActualDeProducto(productoId));
    }

    @GetMapping("/temporada/{temporada}")
    public ResponseEntity<List<PrecioResponseDTO>> listarPorTemporada(@PathVariable Temporada temporada) {
        return ResponseEntity.ok(precioService.listarPorTemporada(temporada));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(precioService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<PrecioResponseDTO> crear(@Valid @RequestBody PrecioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(precioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody PrecioRequestDTO dto) {
        return ResponseEntity.ok(precioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        precioService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
