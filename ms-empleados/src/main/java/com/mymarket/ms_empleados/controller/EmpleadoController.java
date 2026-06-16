package com.mymarket.ms_empleados.controller;

import com.mymarket.ms_empleados.dto.EmpleadoRequestDTO;
import com.mymarket.ms_empleados.dto.EmpleadoResponseDTO;
import com.mymarket.ms_empleados.model.Rol;
import com.mymarket.ms_empleados.model.Turno;
import com.mymarket.ms_empleados.service.EmpleadoService;

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
@RequestMapping("/api/empleados")
@Tag(name = "Empleados", description = "Operaciones relacionadas con la gestion de empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @Operation(summary = "Obtiene todos los empleados", description = "Retorna la lista completa de empleados registrados")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(empleadoService.listarTodos());
    }

    @Operation(summary = "Obtiene empleados activos", description = "Retorna la lista de empleados activos")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/activos")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarActivos() {
        return ResponseEntity.ok(empleadoService.listarActivos());
    }

    @Operation(summary = "Busca empleados por rol", description = "Filtra empleados segun su rol")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarPorRol(@PathVariable Rol rol) {
        return ResponseEntity.ok(empleadoService.listarPorRol(rol));
    }

    @Operation(summary = "Busca empleados por turno", description = "Filtra empleados segun su turno")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Consulta exitosa")})
    @GetMapping("/turno/{turno}")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarPorTurno(@PathVariable Turno turno) {
        return ResponseEntity.ok(empleadoService.listarPorTurno(turno));
    }

    @Operation(summary = "Busca empleado por ID", description = "Retorna un empleado especifico segun su ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Empleado encontrado"), @ApiResponse(responseCode = "404", description = "Empleado no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.buscarPorId(id));
    }

    @Operation(summary = "Busca empleado por usuario", description = "Retorna un empleado asociado a un ID de usuario")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Empleado encontrado"), @ApiResponse(responseCode = "404", description = "Empleado no encontrado")})
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EmpleadoResponseDTO> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(empleadoService.buscarPorUsuarioId(usuarioId));
    }

    @Operation(summary = "Crea un empleado", description = "Permite registrar un nuevo empleado en el sistema")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Empleado creado"), @ApiResponse(responseCode = "400", description = "Datos invalidos")})
    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> crear(@Valid @RequestBody EmpleadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.crear(dto));
    }

    @Operation(summary = "Actualiza un empleado", description = "Actualiza los datos de un empleado existente")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Empleado actualizado"), @ApiResponse(responseCode = "404", description = "Empleado no encontrado")})
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody EmpleadoRequestDTO dto) {
        return ResponseEntity.ok(empleadoService.actualizar(id, dto));
    }

    @Operation(summary = "Desactiva un empleado", description = "Realiza una desactivacion logica de un empleado")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Empleado desactivado"), @ApiResponse(responseCode = "404", description = "Empleado no encontrado")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        empleadoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
