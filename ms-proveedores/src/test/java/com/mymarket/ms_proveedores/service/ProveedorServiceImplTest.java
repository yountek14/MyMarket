package com.mymarket.ms_proveedores.service;

import com.mymarket.ms_proveedores.model.Proveedor;
import com.mymarket.ms_proveedores.repository.ProveedorRepo;
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
class ProveedorServiceImplTest {

    @Mock
    private ProveedorRepo repo;

    @InjectMocks
    private ProveedorServiceImpl service;

    @Test
    void listarTodos_debeRetornarLista() {
        Proveedor p1 = proveedorDePrueba(1L, "Prov A", "11.111.111-1");
        Proveedor p2 = proveedorDePrueba(2L, "Prov B", "22.222.222-2");
        when(repo.findAll()).thenReturn(List.of(p1, p2));

        List<Proveedor> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        verify(repo).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarProveedor() {
        Proveedor p = proveedorDePrueba(1L, "Prov A", "11.111.111-1");
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Proveedor resultado = service.buscarPorId(1L);

        assertEquals("Prov A", resultado.getNombre());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    void guardar_debeConvertirNombreAUppercase() {
        Proveedor p = proveedorDePrueba(null, "prov a", "11.111.111-1");
        Proveedor guardado = proveedorDePrueba(1L, "PROV A", "11.111.111-1");
        when(repo.save(any(Proveedor.class))).thenReturn(guardado);

        Proveedor resultado = service.guardar(p);

        assertEquals("PROV A", resultado.getNombre());
        verify(repo).save(any(Proveedor.class));
    }

    @Test
    void guardar_conNombreNull_noDebeLanzarError() {
        Proveedor p = proveedorDePrueba(null, null, "11.111.111-1");
        Proveedor guardado = proveedorDePrueba(1L, null, "11.111.111-1");
        when(repo.save(any(Proveedor.class))).thenReturn(guardado);

        Proveedor resultado = service.guardar(p);

        assertNull(resultado.getNombre());
    }

    @Test
    void eliminar_debeEliminarProveedorExistente() {
        Proveedor p = proveedorDePrueba(1L, "Prov A", "11.111.111-1");
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.eliminar(99L));
    }

    @Test
    void buscarPorRut_cuandoExiste_debeRetornarProveedor() {
        Proveedor p = proveedorDePrueba(1L, "Prov A", "11.111.111-1");
        when(repo.findByRut("11.111.111-1")).thenReturn(Optional.of(p));

        Proveedor resultado = service.buscarPorRut("11.111.111-1");

        assertEquals("Prov A", resultado.getNombre());
        verify(repo).findByRut("11.111.111-1");
    }

    @Test
    void buscarPorRut_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repo.findByRut("99.999.999-9")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorRut("99.999.999-9"));
    }

    private Proveedor proveedorDePrueba(Long id, String nombre, String rut) {
        Proveedor p = new Proveedor();
        p.setId(id);
        p.setNombre(nombre);
        p.setRut(rut);
        p.setDireccion("Av. Siempre Viva 123");
        p.setTelefono("+56212345678");
        p.setEmail("contacto@test.cl");
        p.setRubro("Tecnologia");
        return p;
    }
}
