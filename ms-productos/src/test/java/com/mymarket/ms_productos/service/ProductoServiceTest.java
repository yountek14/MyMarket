package com.mymarket.ms_productos.service;

import com.mymarket.ms_productos.model.ProductoModel;
import com.mymarket.ms_productos.model.UnidadMedida;
import com.mymarket.ms_productos.repository.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository repository;

    @InjectMocks
    private ProductoService service;

    @Test
    void listarTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(productoDePrueba(1L, "Leche"), productoDePrueba(2L, "Pan")));

        List<ProductoModel> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarProducto() {
        ProductoModel p = productoDePrueba(1L, "Leche");
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        ProductoModel resultado = service.buscarPorId(1L);

        assertEquals("Leche", resultado.getNombreProducto());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    void guardar_conNombreUnico_debeGuardarProducto() {
        ProductoModel p = productoDePrueba(null, "Leche");
        p.setActivo(null);
        ProductoModel guardado = productoDePrueba(1L, "Leche");
        when(repository.existsByNombreProductoIgnoreCase("Leche")).thenReturn(false);
        when(repository.save(any(ProductoModel.class))).thenReturn(guardado);

        ProductoModel resultado = service.guardar(p);

        assertEquals(1L, resultado.getId());
        assertTrue(resultado.getActivo());
        verify(repository).save(any(ProductoModel.class));
    }

    @Test
    void guardar_conNombreDuplicado_debeLanzarIllegalArgumentException() {
        ProductoModel p = productoDePrueba(null, "Leche");
        when(repository.existsByNombreProductoIgnoreCase("Leche")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.guardar(p));
        verify(repository, never()).save(any());
    }

    @Test
    void actualizar_debeModificarProductoExistente() {
        ProductoModel existente = productoDePrueba(1L, "Leche");
        ProductoModel actualizado = productoDePrueba(null, "Leche Entera");
        actualizado.setPrecioBase(1200.0);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.existsByNombreProductoIgnoreCase("Leche Entera")).thenReturn(false);
        when(repository.save(existente)).thenReturn(existente);

        ProductoModel resultado = service.actualizar(1L, actualizado);

        assertEquals("Leche Entera", resultado.getNombreProducto());
        assertEquals(1200.0, resultado.getPrecioBase());
        verify(repository).save(existente);
    }

    @Test
    void actualizar_conNombreDuplicado_debeLanzarIllegalArgumentException() {
        ProductoModel existente = productoDePrueba(1L, "Leche");
        ProductoModel actualizado = productoDePrueba(null, "Queso");
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.existsByNombreProductoIgnoreCase("Queso")).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> service.actualizar(1L, actualizado));
        verify(repository, never()).save(any());
    }

    @Test
    void eliminarLogico_debePonerActivoEnFalse() {
        ProductoModel p = productoDePrueba(1L, "Leche");
        assertTrue(p.getActivo());
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        service.eliminarLogico(1L);

        assertFalse(p.getActivo());
        verify(repository).save(p);
    }

    @Test
    void buscarPorCategoria_debeRetornarFiltrados() {
        when(repository.findByCategoria("Lacteos"))
                .thenReturn(List.of(productoDePrueba(1L, "Leche")));

        List<ProductoModel> resultado = service.buscarPorCategoria("Lacteos");

        assertEquals(1, resultado.size());
        verify(repository).findByCategoria("Lacteos");
    }

    @Test
    void buscarPorActivo_debeRetornarFiltrados() {
        when(repository.findByActivo(true))
                .thenReturn(List.of(productoDePrueba(1L, "Leche")));

        List<ProductoModel> resultado = service.buscarPorActivo(true);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorNombre_debeRetornarCoincidencias() {
        when(repository.findByNombreProductoContainingIgnoreCase("lec"))
                .thenReturn(List.of(productoDePrueba(1L, "Leche")));

        List<ProductoModel> resultado = service.buscarPorNombre("lec");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorUnidadMedida_debeRetornarFiltrados() {
        when(repository.findByUnidadMedida(UnidadMedida.UNIDAD))
                .thenReturn(List.of(productoDePrueba(1L, "Mouse")));

        List<ProductoModel> resultado = service.buscarPorUnidadMedida(UnidadMedida.UNIDAD);

        assertEquals(1, resultado.size());
    }

    private ProductoModel productoDePrueba(Long id, String nombre) {
        return ProductoModel.builder()
                .id(id)
                .nombreProducto(nombre)
                .categoria("Lacteos")
                .unidadMedida(UnidadMedida.UNIDAD)
                .precioBase(1000.0)
                .activo(true)
                .descripcionProducto("Producto de prueba")
                .fechaCaducidad(LocalDate.of(2027, 6, 1))
                .build();
    }
}
