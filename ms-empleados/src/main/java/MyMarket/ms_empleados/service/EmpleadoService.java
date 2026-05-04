package MyMarket.ms_empleados.service;

import MyMarket.ms_empleados.dto.EmpleadoRequestDTO;
import MyMarket.ms_empleados.dto.EmpleadoResponseDTO;
import MyMarket.ms_empleados.model.Empleado;
import MyMarket.ms_empleados.model.Rol;
import MyMarket.ms_empleados.model.Turno;
import MyMarket.ms_empleados.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

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
        Empleado e = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        return toDTO(e);
    }

    public EmpleadoResponseDTO buscarPorUsuarioId(Long usuarioId) {
        Empleado e = empleadoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        return toDTO(e);
    }

    public EmpleadoResponseDTO crear(EmpleadoRequestDTO dto) {
        if (empleadoRepository.findByRut(dto.getRut()).isPresent()) {
            throw new RuntimeException("Ya existe un empleado con ese RUT");
        }
        if (empleadoRepository.findByUsuarioId(dto.getUsuarioId()).isPresent()) {
            throw new RuntimeException("Ese usuario ya tiene un empleado asignado");
        }
        return toDTO(empleadoRepository.save(toEntity(dto)));
    }

    public EmpleadoResponseDTO actualizar(Long id, EmpleadoRequestDTO dto) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        empleado.setNombre(dto.getNombre());
        empleado.setApellido(dto.getApellido());
        empleado.setTelefono(dto.getTelefono());
        empleado.setRol(dto.getRol());
        empleado.setTurno(dto.getTurno());
        return toDTO(empleadoRepository.save(empleado));
    }

    public void desactivar(Long id) {
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
    }
}
