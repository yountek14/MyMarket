package com.mymarket.ms_usuario.service;

import com.mymarket.ms_usuario.enums.Rol;
import com.mymarket.ms_usuario.model.Usuario;
import com.mymarket.ms_usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    void listarTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(new Usuario(), new Usuario()));

        List<Usuario> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarUsuario() {
        Usuario u = usuarioDePrueba(1L, "juan@test.cl", "Juan");
        when(repository.findById(1L)).thenReturn(Optional.of(u));

        Usuario resultado = service.buscarPorId(1L);

        assertEquals("juan@test.cl", resultado.getEmail());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    void crear_debeGuardarYRetornarUsuario() {
        Usuario u = usuarioDePrueba(null, "juan@test.cl", "Juan");
        Usuario guardado = usuarioDePrueba(1L, "juan@test.cl", "Juan");
        when(repository.save(any(Usuario.class))).thenReturn(guardado);

        Usuario resultado = service.crear(u);

        assertEquals(1L, resultado.getId());
        assertEquals("juan@test.cl", resultado.getEmail());
        verify(repository).save(any(Usuario.class));
    }

    @Test
    void actualizar_cuandoExiste_debeModificarDatos() {
        Usuario existente = usuarioDePrueba(1L, "juan@test.cl", "Juan");
        Usuario datos = usuarioDePrueba(null, "juan.nuevo@test.cl", "Juan Perez");
        datos.setRol(Rol.CAJERO);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        Usuario resultado = service.actualizar(1L, datos);

        assertEquals("Juan Perez", resultado.getNombre());
        assertEquals("juan.nuevo@test.cl", resultado.getEmail());
        assertEquals(Rol.CAJERO, resultado.getRol());
        verify(repository).save(existente);
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        Usuario datos = new Usuario();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.actualizar(99L, datos));
    }

    @Test
    void desactivar_debePonerActivoEnFalse() {
        Usuario u = usuarioDePrueba(1L, "juan@test.cl", "Juan");
        assertTrue(u.isActivo());
        when(repository.findById(1L)).thenReturn(Optional.of(u));

        service.desactivar(1L);

        assertFalse(u.isActivo());
        verify(repository).save(u);
    }

    @Test
    void desactivar_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.desactivar(99L));
    }

    private Usuario usuarioDePrueba(Long id, String email, String nombre) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPassword("password123");
        u.setRol(Rol.ADMIN);
        u.setActivo(true);
        return u;
    }
}
