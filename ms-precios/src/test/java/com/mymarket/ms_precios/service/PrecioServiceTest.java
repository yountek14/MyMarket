package com.mymarket.ms_precios.service;

import com.mymarket.ms_precios.dto.PrecioRequestDTO;
import com.mymarket.ms_precios.dto.PrecioResponseDTO;
import com.mymarket.ms_precios.model.Precio;
import com.mymarket.ms_precios.model.Temporada;
import com.mymarket.ms_precios.model.TipoDescuento;
import com.mymarket.ms_precios.repository.PrecioRepository;
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
class PrecioServiceTest {

    @Mock
    private PrecioRepository repository;

    @InjectMocks
    private PrecioService service;

    @Test
    void listarTodos_debeRetornarDTOsConPrecioFinal() {
        Precio p1 = precioDePrueba(1L, 10L, 1000.0, TipoDescuento.PORCENTAJE, 10.0);
        Precio p2 = precioDePrueba(2L, 20L, 500.0, null, null);
        when(repository.findAll()).thenReturn(List.of(p1, p2));

        List<PrecioResponseDTO> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals(900.0, resultado.get(0).getPrecioFinal());
        assertEquals(500.0, resultado.get(1).getPrecioFinal());
    }

    @Test
    void listarActivos_debeRetornarSoloActivos() {
        when(repository.findByActivoTrue())
                .thenReturn(List.of(precioDePrueba(1L, 10L, 1000.0, null, null)));

        List<PrecioResponseDTO> resultado = service.listarActivos();

        assertEquals(1, resultado.size());
        verify(repository).findByActivoTrue();
    }

    @Test
    void listarPorProducto_debeFiltrarCorrectamente() {
        when(repository.findByProductoId(10L))
                .thenReturn(List.of(precioDePrueba(1L, 10L, 1000.0, null, null)));

        List<PrecioResponseDTO> resultado = service.listarPorProducto(10L);

        assertEquals(1, resultado.size());
        assertEquals(10L, resultado.get(0).getProductoId());
    }

    @Test
    void listarPorTemporada_debeFiltrarCorrectamente() {
        when(repository.findByTemporada(Temporada.VERANO))
                .thenReturn(List.of(precioDePrueba(1L, 10L, 1000.0, null, null)));

        List<PrecioResponseDTO> resultado = service.listarPorTemporada(Temporada.VERANO);

        assertEquals(1, resultado.size());
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarDTO() {
        Precio p = precioDePrueba(1L, 10L, 1000.0, TipoDescuento.FIJO, 100.0);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        PrecioResponseDTO resultado = service.buscarPorId(1L);

        assertEquals(900.0, resultado.getPrecioFinal());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    void buscarPrecioActualDeProducto_cuandoExiste_debeRetornarDTO() {
        Precio p = precioDePrueba(1L, 10L, 1000.0, null, null);
        when(repository.findTopByProductoIdAndActivoTrueOrderByFechaInicioDesc(10L))
                .thenReturn(Optional.of(p));

        PrecioResponseDTO resultado = service.buscarPrecioActualDeProducto(10L);

        assertEquals(1000.0, resultado.getPrecioFinal());
    }

    @Test
    void buscarPrecioActualDeProducto_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repository.findTopByProductoIdAndActivoTrueOrderByFechaInicioDesc(99L))
                .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPrecioActualDeProducto(99L));
    }

    @Test
    void crear_debeGuardarYRetornarDTO() {
        PrecioRequestDTO request = requestDePrueba(10L, 1000.0);
        Precio guardado = precioDePrueba(1L, 10L, 1000.0, null, null);
        when(repository.save(any(Precio.class))).thenReturn(guardado);

        PrecioResponseDTO resultado = service.crear(request);

        assertNotNull(resultado);
        assertEquals(10L, resultado.getProductoId());
        verify(repository).save(any(Precio.class));
    }

    @Test
    void actualizar_cuandoExiste_debeModificarYRetornarDTO() {
        Precio existente = precioDePrueba(1L, 10L, 800.0, null, null);
        PrecioRequestDTO request = requestDePrueba(10L, 1200.0);
        request.setTipoDescuento(TipoDescuento.PORCENTAJE);
        request.setValorDescuento(20.0);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        PrecioResponseDTO resultado = service.actualizar(1L, request);

        assertEquals(960.0, resultado.getPrecioFinal());
        verify(repository).save(existente);
    }

    @Test
    void actualizar_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        PrecioRequestDTO request = requestDePrueba(10L, 1000.0);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.actualizar(99L, request));
    }

    @Test
    void desactivar_debePonerActivoEnFalse() {
        Precio p = precioDePrueba(1L, 10L, 1000.0, null, null);
        assertTrue(p.isActivo());
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        service.desactivar(1L);

        assertFalse(p.isActivo());
        verify(repository).save(p);
    }

    @Test
    void calcularPrecioFinal_descuentoPorcentaje_debeRestarPorcentaje() {
        Precio p = precioDePrueba(1L, 10L, 1000.0, TipoDescuento.PORCENTAJE, 25.0);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        PrecioResponseDTO resultado = service.buscarPorId(1L);

        assertEquals(750.0, resultado.getPrecioFinal());
    }

    @Test
    void calcularPrecioFinal_descuentoFijo_debeRestarMonto() {
        Precio p = precioDePrueba(1L, 10L, 1000.0, TipoDescuento.FIJO, 300.0);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        PrecioResponseDTO resultado = service.buscarPorId(1L);

        assertEquals(700.0, resultado.getPrecioFinal());
    }

    @Test
    void calcularPrecioFinal_sinDescuento_debeRetornarPrecioBase() {
        Precio p = precioDePrueba(1L, 10L, 1000.0, null, null);
        when(repository.findById(1L)).thenReturn(Optional.of(p));

        PrecioResponseDTO resultado = service.buscarPorId(1L);

        assertEquals(1000.0, resultado.getPrecioFinal());
    }

    private Precio precioDePrueba(Long id, Long productoId, Double precioBase,
                                   TipoDescuento tipoDescuento, Double valorDescuento) {
        Precio p = new Precio();
        p.setId(id);
        p.setProductoId(productoId);
        p.setPrecioBase(precioBase);
        p.setTipoDescuento(tipoDescuento);
        p.setValorDescuento(valorDescuento);
        p.setTemporada(Temporada.VERANO);
        p.setFechaInicio(LocalDate.of(2026, 1, 1));
        p.setFechaFin(LocalDate.of(2026, 3, 31));
        p.setActivo(true);
        return p;
    }

    private PrecioRequestDTO requestDePrueba(Long productoId, Double precioBase) {
        PrecioRequestDTO dto = new PrecioRequestDTO();
        dto.setProductoId(productoId);
        dto.setPrecioBase(precioBase);
        dto.setTipoDescuento(null);
        dto.setValorDescuento(null);
        dto.setTemporada(Temporada.VERANO);
        dto.setFechaInicio(LocalDate.of(2026, 1, 1));
        dto.setFechaFin(LocalDate.of(2026, 3, 31));
        return dto;
    }
}
