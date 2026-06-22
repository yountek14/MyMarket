package com.mymarket.ms_empleados.service;

import com.mymarket.ms_empleados.dto.EmpleadoRequestDTO;
import com.mymarket.ms_empleados.dto.EmpleadoResponseDTO;
import com.mymarket.ms_empleados.model.Empleado;
import com.mymarket.ms_empleados.model.Rol;
import com.mymarket.ms_empleados.model.Turno;
import com.mymarket.ms_empleados.repository.EmpleadoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository repository;

    @InjectMocks
    private EmpleadoService service;

    @Test
    void listarTodos_debeRetornarListaDeDTOs() {
        Empleado e1 = empleadoDePrueba(1L, "Ana", "11.111.111-1");
        Empleado e2 = empleadoDePrueba(2L, "Luis", "22.222.222-2");
        when(repository.findAll()).thenReturn(List.of(e1, e2));

        List<EmpleadoResponseDTO> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombre());
        assertEquals("Luis", resultado.get(1).getNombre());
        verify(repository).findAll();
    }

    @Test
    void listarActivos_debeRetornarSoloActivos() {
        when(repository.findByActivo(true))
                .thenReturn(List.of(empleadoDePrueba(1L, "Ana", "11.111.111-1")));

        List<EmpleadoResponseDTO> resultado = service.listarActivos();

        assertEquals(1, resultado.size());
        assertTrue(resultado.get(0).isActivo());
        verify(repository).findByActivo(true);
    }

    @Test
    void listarPorRol_debeFiltrarCorrectamente() {
        when(repository.findByRol(Rol.CAJERO))
                .thenReturn(List.of(empleadoDePrueba(1L, "Ana", "11.111.111-1")));

        List<EmpleadoResponseDTO> resultado = service.listarPorRol(Rol.CAJERO);

        assertEquals(1, resultado.size());
        assertEquals(Rol.CAJERO, resultado.get(0).getRol());
    }

    @Test
    void listarPorTurno_debeFiltrarCorrectamente() {
        when(repository.findByTurno(Turno.MAÑANA))
                .thenReturn(List.of(empleadoDePrueba(1L, "Ana", "11.111.111-1")));

        List<EmpleadoResponseDTO> resultado = service.listarPorTurno(Turno.MAÑANA);

        assertEquals(1, resultado.size());
        assertEquals(Turno.MAÑANA, resultado.get(0).getTurno());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarDTO() {
        Empleado e = empleadoDePrueba(1L, "Ana", "11.111.111-1");
        when(repository.findById(1L)).thenReturn(Optional.of(e));

        EmpleadoResponseDTO resultado = service.buscarPorId(1L);

        assertEquals("Ana", resultado.getNombre());
        assertEquals("11.111.111-1", resultado.getRut());
        verify(repository).findById(1L);
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    void buscarPorUsuarioId_cuandoExiste_debeRetornarDTO() {
        Empleado e = empleadoDePrueba(1L, "Ana", "11.111.111-1");
        when(repository.findByUsuarioId(10L)).thenReturn(Optional.of(e));

        EmpleadoResponseDTO resultado = service.buscarPorUsuarioId(10L);

        assertEquals("Ana", resultado.getNombre());
    }

    @Test
    void buscarPorUsuarioId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findByUsuarioId(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorUsuarioId(99L));
    }

    @Test
    void crear_conDatosUnicos_debeGuardarYRetornarDTO() {
        EmpleadoRequestDTO request = requestDePrueba("11.111.111-1", 10L);
        Empleado guardado = empleadoDePrueba(1L, "Ana", "11.111.111-1");
        when(repository.findByRut("11.111.111-1")).thenReturn(Optional.empty());
        when(repository.findByUsuarioId(10L)).thenReturn(Optional.empty());
        when(repository.save(any(Empleado.class))).thenReturn(guardado);

        EmpleadoResponseDTO resultado = service.crear(request);

        assertNotNull(resultado);
        assertEquals("Ana", resultado.getNombre());
        verify(repository).save(any(Empleado.class));
    }

    @Test
    void crear_conRUTDuplicado_debeLanzarIllegalArgumentException() {
        EmpleadoRequestDTO request = requestDePrueba("11.111.111-1", 10L);
        when(repository.findByRut("11.111.111-1"))
                .thenReturn(Optional.of(new Empleado()));

        assertThrows(IllegalArgumentException.class,
                () -> service.crear(request));
        verify(repository, never()).save(any());
    }

    @Test
    void crear_conUsuarioIdDuplicado_debeLanzarIllegalArgumentException() {
        EmpleadoRequestDTO request = requestDePrueba("11.111.111-1", 10L);
        when(repository.findByRut("11.111.111-1")).thenReturn(Optional.empty());
        when(repository.findByUsuarioId(10L))
                .thenReturn(Optional.of(new Empleado()));

        assertThrows(IllegalArgumentException.class,
                () -> service.crear(request));
        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_cuandoExiste_debeModificarYRetornarDTO() {
        Empleado existente = empleadoDePrueba(1L, "Ana", "11.111.111-1");
        EmpleadoRequestDTO request = requestDePrueba("11.111.111-1", 10L);
        request.setNombre("Mariana");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        EmpleadoResponseDTO resultado = service.actualizar(1L, request);

        assertEquals("Mariana", resultado.getNombre());
        verify(repository).save(existente);
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        EmpleadoRequestDTO request = requestDePrueba("11.111.111-1", 10L);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.actualizar(99L, request));
    }

    @Test
    void desactivar_debePonerActivoEnFalse() {
        Empleado e = empleadoDePrueba(1L, "Ana", "11.111.111-1");
        assertTrue(e.isActivo());
        when(repository.findById(1L)).thenReturn(Optional.of(e));

        service.desactivar(1L);

        assertFalse(e.isActivo());
        verify(repository).save(e);
    }

    @Test
    void desactivar_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.desactivar(99L));
    }

    private Empleado empleadoDePrueba(Long id, String nombre, String rut) {
        Empleado e = new Empleado();
        e.setId(id);
        e.setNombre(nombre);
        e.setApellido("Test");
        e.setRut(rut);
        e.setTelefono("+56912345678");
        e.setRol(Rol.CAJERO);
        e.setTurno(Turno.MAÑANA);
        e.setFechaContratacion(LocalDate.of(2026, 1, 15));
        e.setActivo(true);
        e.setUsuarioId(10L);
        return e;
    }

    private EmpleadoRequestDTO requestDePrueba(String rut, Long usuarioId) {
        EmpleadoRequestDTO dto = new EmpleadoRequestDTO();
        dto.setNombre("Ana");
        dto.setApellido("Test");
        dto.setRut(rut);
        dto.setTelefono("+56912345678");
        dto.setRol(Rol.CAJERO);
        dto.setTurno(Turno.MAÑANA);
        dto.setFechaContratacion(LocalDate.of(2026, 1, 15));
        dto.setUsuarioId(usuarioId);
        return dto;
    }
}
