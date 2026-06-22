package com.mymarket.ms_ventas.service;

import com.mymarket.ms_ventas.model.EstadoVenta;
import com.mymarket.ms_ventas.model.VentaModel;
import com.mymarket.ms_ventas.repository.VentaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private VentaService service;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
        ReflectionTestUtils.setField(service, "productosUrl", "http://localhost:8087/api/v1/productos/");
        ReflectionTestUtils.setField(service, "inventarioUrl", "http://localhost:8083/api/v1/inventario/");
    }

    @Test
    void listarTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(
                ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA),
                ventaDePrueba(2L, 20L, EstadoVenta.PAGADA)
        ));

        List<VentaModel> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarVenta() {
        VentaModel v = ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA);
        when(repository.findById(1L)).thenReturn(Optional.of(v));

        VentaModel resultado = service.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    void actualizar_debeModificarYRecalcularTotal() {
        VentaModel existente = ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA);
        VentaModel datos = VentaModel.builder()
                .productoId(20L)
                .inventarioId(5L)
                .cantidadVendida(5)
                .precioUnitario(2000.0)
                .estado(EstadoVenta.PAGADA)
                .activo(true)
                .build();
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        VentaModel resultado = service.actualizar(1L, datos);

        assertEquals(20L, resultado.getProductoId());
        assertEquals(5, resultado.getCantidadVendida());
        assertEquals(10000.0, resultado.getTotalVenta());
        assertEquals(EstadoVenta.PAGADA, resultado.getEstado());
        verify(repository).save(existente);
    }

    @Test
    void eliminarLogico_debePonerActivoEnFalse() {
        VentaModel v = ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA);
        assertTrue(v.getActivo());
        when(repository.findById(1L)).thenReturn(Optional.of(v));

        service.eliminarLogico(1L);

        assertFalse(v.getActivo());
        verify(repository).save(v);
    }

    @Test
    void marcarComoPagada_debeCambiarEstado() {
        VentaModel v = ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA);
        assertEquals(EstadoVenta.REGISTRADA, v.getEstado());
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(repository.save(v)).thenReturn(v);

        VentaModel resultado = service.marcarComoPagada(1L);

        assertEquals(EstadoVenta.PAGADA, resultado.getEstado());
    }

    @Test
    void anularVenta_debeCambiarEstadoAAnulada() {
        VentaModel v = ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA);
        when(repository.findById(1L)).thenReturn(Optional.of(v));
        when(repository.save(v)).thenReturn(v);

        VentaModel resultado = service.anularVenta(1L);

        assertEquals(EstadoVenta.ANULADA, resultado.getEstado());
    }

    @Test
    void buscarPorProductoId_debeRetornarFiltrados() {
        when(repository.findByProductoId(10L))
                .thenReturn(List.of(ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA)));

        List<VentaModel> resultado = service.buscarPorProductoId(10L);

        assertEquals(1, resultado.size());
        verify(repository).findByProductoId(10L);
    }

    @Test
    void buscarPorInventarioId_debeRetornarFiltrados() {
        when(repository.findByInventarioId(5L))
                .thenReturn(List.of(ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA)));

        List<VentaModel> resultado = service.buscarPorInventarioId(5L);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorEstado_debeRetornarFiltrados() {
        when(repository.findByEstado(EstadoVenta.PAGADA))
                .thenReturn(List.of(ventaDePrueba(2L, 20L, EstadoVenta.PAGADA)));

        List<VentaModel> resultado = service.buscarPorEstado(EstadoVenta.PAGADA);

        assertEquals(1, resultado.size());
        assertEquals(EstadoVenta.PAGADA, resultado.get(0).getEstado());
    }

    @Test
    void buscarPorActivo_debeRetornarFiltrados() {
        when(repository.findByActivo(true))
                .thenReturn(List.of(ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA)));

        List<VentaModel> resultado = service.buscarPorActivo(true);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorRangoFechas_debeRetornarFiltrados() {
        LocalDateTime inicio = LocalDateTime.of(2026, 6, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2026, 6, 30, 23, 59);
        when(repository.findByFechaVentaBetween(inicio, fin))
                .thenReturn(List.of(ventaDePrueba(1L, 10L, EstadoVenta.REGISTRADA)));

        List<VentaModel> resultado = service.buscarPorRangoFechas(inicio, fin);

        assertEquals(1, resultado.size());
    }

    private VentaModel ventaDePrueba(Long id, Long productoId, EstadoVenta estado) {
        return VentaModel.builder()
                .id(id)
                .productoId(productoId)
                .inventarioId(5L)
                .cantidadVendida(2)
                .precioUnitario(5000.0)
                .totalVenta(10000.0)
                .fechaVenta(LocalDateTime.of(2026, 6, 15, 10, 30))
                .activo(true)
                .estado(estado)
                .build();
    }
}
