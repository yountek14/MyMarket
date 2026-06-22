package com.mymarket.ms_inventario.service;

import com.mymarket.ms_inventario.model.EstadoInventario;
import com.mymarket.ms_inventario.model.InventarioModel;
import com.mymarket.ms_inventario.repository.InventarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private InventarioService service;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
        ReflectionTestUtils.setField(service, "productosUrl", "http://localhost:8087/api/v1/productos/");
    }

    private static InventarioModel inventarioDePrueba(Long id, String lote, Integer stockActual,
                                                        Integer stockMinimo, Integer stockMaximo,
                                                        LocalDate fechaVencimiento) {
        return InventarioModel.builder()
                .id(id)
                .productoId(1L)
                .lote(lote)
                .stockActual(stockActual)
                .stockMinimo(stockMinimo)
                .stockMaximo(stockMaximo)
                .merma(0)
                .fechaIngreso(LocalDate.of(2026, 1, 15))
                .fechaVencimiento(fechaVencimiento)
                .estado(EstadoInventario.DISPONIBLE)
                .activo(true)
                .build();
    }

    @Test
    void listarTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(
                inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.of(2027, 6, 1)),
                inventarioDePrueba(2L, "LOTE-002", 50, 5, 300, LocalDate.of(2027, 8, 1))
        ));

        List<InventarioModel> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarInventario() {
        InventarioModel i = inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.of(2027, 6, 1));
        when(repository.findById(1L)).thenReturn(Optional.of(i));

        InventarioModel resultado = service.buscarPorId(1L);

        assertEquals("LOTE-001", resultado.getLote());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    void registrarEntrada_debeAumentarStock() {
        InventarioModel i = inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.of(2027, 6, 1));
        when(repository.findById(1L)).thenReturn(Optional.of(i));
        when(repository.save(i)).thenReturn(i);

        InventarioModel resultado = service.registrarEntrada(1L, 50);

        assertEquals(150, resultado.getStockActual());
        verify(repository).save(i);
    }

    @Test
    void registrarEntrada_conCantidadInvalida_debeLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registrarEntrada(1L, -5));
        assertThrows(IllegalArgumentException.class,
                () -> service.registrarEntrada(1L, 0));
        assertThrows(IllegalArgumentException.class,
                () -> service.registrarEntrada(1L, null));
    }

    @Test
    void registrarSalida_debeDisminuirStock() {
        InventarioModel i = inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.of(2027, 6, 1));
        when(repository.findById(1L)).thenReturn(Optional.of(i));
        when(repository.save(i)).thenReturn(i);

        InventarioModel resultado = service.registrarSalida(1L, 30);

        assertEquals(70, resultado.getStockActual());
    }

    @Test
    void registrarSalida_conStockInsuficiente_debeLanzarIllegalArgumentException() {
        InventarioModel i = inventarioDePrueba(1L, "LOTE-001", 10, 5, 500, LocalDate.of(2027, 6, 1));
        when(repository.findById(1L)).thenReturn(Optional.of(i));

        assertThrows(IllegalArgumentException.class,
                () -> service.registrarSalida(1L, 20));
    }

    @Test
    void registrarSalida_conCantidadInvalida_debeLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.registrarSalida(1L, -1));
        assertThrows(IllegalArgumentException.class,
                () -> service.registrarSalida(1L, 0));
    }

    @Test
    void registrarMerma_debeAumentarMermaYDisminuirStock() {
        InventarioModel i = inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.of(2027, 6, 1));
        when(repository.findById(1L)).thenReturn(Optional.of(i));
        when(repository.save(i)).thenReturn(i);

        InventarioModel resultado = service.registrarMerma(1L, 20);

        assertEquals(20, resultado.getMerma());
        assertEquals(80, resultado.getStockActual());
    }

    @Test
    void registrarMerma_excedeStock_debeLanzarIllegalArgumentException() {
        InventarioModel i = inventarioDePrueba(1L, "LOTE-001", 10, 5, 500, LocalDate.of(2027, 6, 1));
        when(repository.findById(1L)).thenReturn(Optional.of(i));

        assertThrows(IllegalArgumentException.class,
                () -> service.registrarMerma(1L, 20));
    }

    @Test
    void eliminarLogico_debePonerActivoEnFalse() {
        InventarioModel i = inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.of(2027, 6, 1));
        assertTrue(i.getActivo());
        when(repository.findById(1L)).thenReturn(Optional.of(i));

        service.eliminarLogico(1L);

        assertFalse(i.getActivo());
        verify(repository).save(i);
    }

    @Test
    void buscarPorProductoId_debeRetornarFiltrados() {
        when(repository.findByProductoId(1L))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.of(2027, 6, 1))));

        List<InventarioModel> resultado = service.buscarPorProductoId(1L);

        assertEquals(1, resultado.size());
        verify(repository).findByProductoId(1L);
    }

    @Test
    void buscarPorLote_debeRetornarFiltrados() {
        when(repository.findByLote("LOTE-001"))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.of(2027, 6, 1))));

        List<InventarioModel> resultado = service.buscarPorLote("LOTE-001");

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorEstado_debeRetornarFiltrados() {
        when(repository.findByEstado(EstadoInventario.BAJO_STOCK))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001", 5, 10, 500, LocalDate.of(2027, 6, 1))));

        List<InventarioModel> resultado = service.buscarPorEstado(EstadoInventario.BAJO_STOCK);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorActivo_debeRetornarFiltrados() {
        when(repository.findByActivo(true))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.of(2027, 6, 1))));

        List<InventarioModel> resultado = service.buscarPorActivo(true);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarVencidos_debeRetornarProductosVencidos() {
        when(repository.findByFechaVencimientoBefore(any(LocalDate.class)))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001", 5, 10, 500, LocalDate.of(2025, 1, 1))));

        List<InventarioModel> resultado = service.buscarVencidos();

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorVencer_debeUsarDiasPorDefault() {
        List<InventarioModel> esperados = List.of(
                inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.now().plusDays(3))
        );
        when(repository.findByFechaVencimientoBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(esperados);

        List<InventarioModel> resultado = service.buscarPorVencer(null);

        assertEquals(1, resultado.size());
        assertEquals("LOTE-001", resultado.get(0).getLote());
    }

    @Test
    void buscarPorVencer_conDiasEspecificos_debeUsarEseRango() {
        List<InventarioModel> esperados = List.of(
                inventarioDePrueba(1L, "LOTE-001", 100, 10, 500, LocalDate.now().plusDays(10))
        );
        when(repository.findByFechaVencimientoBetween(any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(esperados);

        List<InventarioModel> resultado = service.buscarPorVencer(15);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarStockBajo_debeRetornarBajoLimite() {
        when(repository.findByStockActualLessThanEqual(20))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001", 5, 10, 500, LocalDate.of(2027, 6, 1))));

        List<InventarioModel> resultado = service.buscarStockBajo(20);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarStockBajo_conValorNegativo_debeLanzarIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class,
                () -> service.buscarStockBajo(-1));
    }
}
