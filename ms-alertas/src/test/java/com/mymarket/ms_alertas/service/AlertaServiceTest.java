package com.mymarket.ms_alertas.service;

import com.mymarket.ms_alertas.model.AlertaModel;
import com.mymarket.ms_alertas.model.EstadoAlerta;
import com.mymarket.ms_alertas.model.TipoAlerta;
import com.mymarket.ms_alertas.repository.AlertaRepository;
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
class AlertaServiceTest {

    @Mock
    private AlertaRepository repository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private AlertaService service;

    @BeforeEach
    void setUp() {
        lenient().when(webClientBuilder.build()).thenReturn(webClient);
        ReflectionTestUtils.setField(service, "productosUrl", "http://localhost:8087/api/v1/productos/");
        ReflectionTestUtils.setField(service, "inventarioUrl", "http://localhost:8083/api/v1/inventario/");
    }

    @Test
    void listarTodas_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(
                alertaDePrueba(1L, 10L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA),
                alertaDePrueba(2L, 20L, TipoAlerta.PRODUCTO_VENCIDO, EstadoAlerta.PENDIENTE)
        ));

        List<AlertaModel> resultado = service.listarTodas();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarAlerta() {
        AlertaModel a = alertaDePrueba(1L, 10L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA);
        when(repository.findById(1L)).thenReturn(Optional.of(a));

        AlertaModel resultado = service.buscarPorId(1L);

        assertEquals(TipoAlerta.STOCK_BAJO, resultado.getTipoAlerta());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    void crearAlertaManual_debeAsignarDefaultsYGuardar() {
        AlertaModel a = AlertaModel.builder()
                .productoId(10L)
                .inventarioId(5L)
                .tipoAlerta(TipoAlerta.STOCK_BAJO)
                .mensaje("Stock bajo detectado")
                .build();
        AlertaModel guardada = alertaDePrueba(1L, 10L, TipoAlerta.STOCK_BAJO, EstadoAlerta.PENDIENTE);
        when(repository.save(any(AlertaModel.class))).thenReturn(guardada);

        AlertaModel resultado = service.crearAlertaManual(a);

        assertEquals(1L, resultado.getId());
        assertTrue(resultado.getActivo());
        assertNotNull(resultado.getFechaCreacion());
        assertEquals(EstadoAlerta.PENDIENTE, resultado.getEstadoAlerta());
        verify(repository).save(any(AlertaModel.class));
    }

    @Test
    void crearAlertaManual_conEstadoYaAsignado_debeMantenerlo() {
        AlertaModel a = AlertaModel.builder()
                .productoId(10L)
                .inventarioId(5L)
                .tipoAlerta(TipoAlerta.STOCK_BAJO)
                .estadoAlerta(EstadoAlerta.ACTIVA)
                .mensaje("Stock bajo")
                .build();
        AlertaModel guardada = alertaDePrueba(1L, 10L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA);
        when(repository.save(any(AlertaModel.class))).thenReturn(guardada);

        AlertaModel resultado = service.crearAlertaManual(a);

        assertEquals(EstadoAlerta.ACTIVA, resultado.getEstadoAlerta());
    }

    @Test
    void resolverAlerta_debeCambiarEstadoYAsignarFechaResolucion() {
        AlertaModel a = alertaDePrueba(1L, 10L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA);
        assertNull(a.getFechaResolucion());
        when(repository.findById(1L)).thenReturn(Optional.of(a));
        when(repository.save(a)).thenReturn(a);

        AlertaModel resultado = service.resolverAlerta(1L);

        assertEquals(EstadoAlerta.RESUELTA, resultado.getEstadoAlerta());
        assertNotNull(resultado.getFechaResolucion());
    }

    @Test
    void eliminarLogico_debePonerActivoEnFalse() {
        AlertaModel a = alertaDePrueba(1L, 10L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA);
        assertTrue(a.getActivo());
        when(repository.findById(1L)).thenReturn(Optional.of(a));

        service.eliminarLogico(1L);

        assertFalse(a.getActivo());
        verify(repository).save(a);
    }

    @Test
    void buscarPorProductoId_debeRetornarFiltrados() {
        when(repository.findByProductoId(10L))
                .thenReturn(List.of(alertaDePrueba(1L, 10L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA)));

        List<AlertaModel> resultado = service.buscarPorProductoId(10L);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorInventarioId_debeRetornarFiltrados() {
        when(repository.findByInventarioId(5L))
                .thenReturn(List.of(alertaDePrueba(1L, 10L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA)));

        List<AlertaModel> resultado = service.buscarPorInventarioId(5L);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorTipo_debeRetornarFiltrados() {
        when(repository.findByTipoAlerta(TipoAlerta.PRODUCTO_VENCIDO))
                .thenReturn(List.of(alertaDePrueba(2L, 20L, TipoAlerta.PRODUCTO_VENCIDO, EstadoAlerta.PENDIENTE)));

        List<AlertaModel> resultado = service.buscarPorTipo(TipoAlerta.PRODUCTO_VENCIDO);

        assertEquals(1, resultado.size());
        assertEquals(TipoAlerta.PRODUCTO_VENCIDO, resultado.get(0).getTipoAlerta());
    }

    @Test
    void buscarPorEstado_debeRetornarFiltrados() {
        when(repository.findByEstadoAlerta(EstadoAlerta.RESUELTA))
                .thenReturn(List.of(alertaDePrueba(3L, 30L, TipoAlerta.STOCK_AGOTADO, EstadoAlerta.RESUELTA)));

        List<AlertaModel> resultado = service.buscarPorEstado(EstadoAlerta.RESUELTA);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorActivo_debeRetornarFiltrados() {
        when(repository.findByActivo(true))
                .thenReturn(List.of(alertaDePrueba(1L, 10L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA)));

        List<AlertaModel> resultado = service.buscarPorActivo(true);

        assertEquals(1, resultado.size());
    }

    private AlertaModel alertaDePrueba(Long id, Long productoId, TipoAlerta tipo, EstadoAlerta estado) {
        return AlertaModel.builder()
                .id(id)
                .productoId(productoId)
                .inventarioId(5L)
                .tipoAlerta(tipo)
                .estadoAlerta(estado)
                .mensaje("Mensaje de alerta de prueba")
                .fechaCreacion(LocalDateTime.of(2026, 6, 15, 10, 0))
                .activo(true)
                .build();
    }
}
