package com.mymarket.ms_reportes.service;

import com.mymarket.ms_reportes.model.Reporte;
import com.mymarket.ms_reportes.repository.ReporteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository repository;

    @InjectMocks
    private ReporteService service;

    @Test
    void obtenerTodos_debeRetornarLista() {
        when(repository.findAll()).thenReturn(List.of(new Reporte(), new Reporte()));

        List<Reporte> resultado = service.obtenerTodos();

        assertEquals(2, resultado.size());
        verify(repository).findAll();
    }

    @Test
    void crearReporte_debeAsignarFechaYGuardar() {
        Reporte r = new Reporte();
        r.setTipo("VENTAS");
        r.setMontoTotal(5000000.0);
        Reporte guardado = new Reporte();
        guardado.setId(1L);
        guardado.setTipo("VENTAS");
        guardado.setGeneradoEn(LocalDateTime.now());
        when(repository.save(any(Reporte.class))).thenReturn(guardado);

        Reporte resultado = service.crearReporte(r);

        assertNotNull(resultado.getGeneradoEn());
        assertEquals(1L, resultado.getId());
        assertEquals("VENTAS", resultado.getTipo());
        verify(repository).save(any(Reporte.class));
    }
}
