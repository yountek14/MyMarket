package MyMarket.ms_empleados.controller;

import MyMarket.ms_empleados.dto.EmpleadoRequestDTO;
import MyMarket.ms_empleados.dto.EmpleadoResponseDTO;
import MyMarket.ms_empleados.model.Rol;
import MyMarket.ms_empleados.model.Turno;
import MyMarket.ms_empleados.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @GetMapping
    public ResponseEntity<List<EmpleadoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(empleadoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarActivos() {
        return ResponseEntity.ok(empleadoService.listarActivos());
    }

    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarPorRol(@PathVariable Rol rol) {
        return ResponseEntity.ok(empleadoService.listarPorRol(rol));
    }

    @GetMapping("/turno/{turno}")
    public ResponseEntity<List<EmpleadoResponseDTO>> listarPorTurno(@PathVariable Turno turno) {
        return ResponseEntity.ok(empleadoService.listarPorTurno(turno));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empleadoService.buscarPorId(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<EmpleadoResponseDTO> buscarPorUsuarioId(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(empleadoService.buscarPorUsuarioId(usuarioId));
    }

    @PostMapping
    public ResponseEntity<EmpleadoResponseDTO> crear(@Valid @RequestBody EmpleadoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(empleadoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponseDTO> actualizar(@PathVariable Long id,
                                                          @Valid @RequestBody EmpleadoRequestDTO dto) {
        return ResponseEntity.ok(empleadoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        empleadoService.desactivar(id);
        return ResponseEntity.noContent().build();
    }
}
