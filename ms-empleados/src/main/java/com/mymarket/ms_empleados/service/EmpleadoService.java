package com.mymarket.ms_empleados.service;

import com.mymarket.ms_empleados.dto.EmpleadoRequestDTO;
import com.mymarket.ms_empleados.dto.EmpleadoResponseDTO;
import com.mymarket.ms_empleados.model.Empleado;
import com.mymarket.ms_empleados.model.Rol;
import com.mymarket.ms_empleados.model.Turno;
import com.mymarket.ms_empleados.repository.EmpleadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio de gestion de empleados. Trabaja con DTOs (Request/Response) para
 * separar la entrada de la salida. Valida unicidad de RUT y usuarioId por empleado.
 */
@Service
public class EmpleadoService {

    private static final Logger log = LoggerFactory.getLogger(EmpleadoService.class);

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    /** Convierte DTO de entrada a entidad JPA. */
    private Empleado toEntity(EmpleadoRequestDTO dto){
        Empleado e = new Empleado();
        e.setNombre(dto.getNombre());
        e.setApellido(dto.getApellido());
        e.setRut(dto.getRut());
        e.setTelefono(dto.getTelefono());
        e.setRol(dto.getRol());
        e.setTurno(dto.getTurno());
        e.setFechaContratacion(dto.getFechaContratacion());
        e.setUsuarioId(dto.getUsuarioId());
        return e;
    }
    /** Convierte entidad JPA a DTO de respuesta (oculta datos sensibles). */
    private EmpleadoResponseDTO toDTO(Empleado e){
        EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setRut(e.getRut());
        dto.setTelefono(e.getTelefono());
        dto.setRol(e.getRol());
        dto.setTurno(e.getTurno());
        dto.setFechaContratacion(e.getFechaContratacion());
        dto.setActivo(e.isActivo());
        dto.setUsuarioId(e.getUsuarioId());
        return dto;
    }
    public List<EmpleadoResponseDTO> listarTodos() {
        return empleadoRepository.findAll()
                .stream().map(this::toDTO).toList();
    }

    public List<EmpleadoResponseDTO> listarActivos() {
        return empleadoRepository.findByActivo(true)
                .stream().map(this::toDTO).toList();
    }

    public List<EmpleadoResponseDTO> listarPorRol(Rol rol) {
        return empleadoRepository.findByRol(rol)
                .stream().map(this::toDTO).toList();
    }

    public List<EmpleadoResponseDTO> listarPorTurno(Turno turno) {
        return empleadoRepository.findByTurno(turno)
                .stream().map(this::toDTO).toList();
    }

    public EmpleadoResponseDTO buscarPorId(Long id) {
        log.info("Buscando empleado por id: {}", id);
        Empleado e = empleadoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Empleado no encontrado con id: {}", id);
                    return new EntityNotFoundException("Empleado no encontrado con id: " + id);
                });
        return toDTO(e);
    }

    public EmpleadoResponseDTO buscarPorUsuarioId(Long usuarioId) {
        log.info("Buscando empleado por usuarioId: {}", usuarioId);
        Empleado e = empleadoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> {
                    log.warn("Empleado no encontrado con usuarioId: {}", usuarioId);
                    return new EntityNotFoundException("Empleado no encontrado con usuarioId: " + usuarioId);
                });
        return toDTO(e);
    }

    public EmpleadoResponseDTO crear(EmpleadoRequestDTO dto) {
        if (empleadoRepository.findByRut(dto.getRut()).isPresent()) {
            log.warn("Intento de crear empleado con RUT duplicado: {}", dto.getRut());
            throw new IllegalArgumentException("Ya existe un empleado con ese RUT");
        }
        if (empleadoRepository.findByUsuarioId(dto.getUsuarioId()).isPresent()) {
            log.warn("Intento de crear empleado con usuarioId duplicado: {}", dto.getUsuarioId());
            throw new IllegalArgumentException("Ese usuario ya tiene un empleado asignado");
        }
        EmpleadoResponseDTO creado = toDTO(empleadoRepository.save(toEntity(dto)));
        log.info("Empleado creado con id: {}, rut: {}", creado.getId(), dto.getRut());
        return creado;
    }

    public EmpleadoResponseDTO actualizar(Long id, EmpleadoRequestDTO dto) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Empleado no encontrado para actualizar - id: {}", id);
                    return new EntityNotFoundException("Empleado no encontrado con id: " + id);
                });
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setTelefono(dto.getTelefono());
        empleado.setRol(dto.getRol());
        empleado.setTurno(dto.getTurno());
        EmpleadoResponseDTO actualizado = toDTO(empleadoRepository.save(empleado));
        log.info("Empleado actualizado con id: {}", id);
        return actualizado;
    }

    public void desactivar(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Empleado no encontrado para desactivar - id: {}", id);
                    return new EntityNotFoundException("Empleado no encontrado con id: " + id);
                });
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
        log.info("Empleado desactivado con id: {}", id);
    }
}
