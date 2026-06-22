package com.mymarket.ms_reportes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_reportes.model.Reporte;
import com.mymarket.ms_reportes.service.ReporteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReporteControllerTest {

    @Mock
    private ReporteService service;

    @InjectMocks
    private ReporteController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listar_debeRetornar200() throws Exception {
        when(service.obtenerTodos()).thenReturn(List.of(reporteDePrueba(1L)));

        mockMvc.perform(get("/api/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipo").value("VENTAS"));
    }

    @Test
    void guardar_debeRetornar200() throws Exception {
        Reporte r = reporteDePrueba(null);
        Reporte guardado = reporteDePrueba(1L);
        when(service.crearReporte(any(Reporte.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/reportes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(r)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    private Reporte reporteDePrueba(Long id) {
        Reporte r = new Reporte();
        r.setId(id);
        r.setTipo("VENTAS");
        r.setMontoTotal(5000000.0);
        r.setGeneradoEn(LocalDateTime.of(2026, 6, 15, 10, 0));
        r.setPeriodoInicio(LocalDateTime.of(2026, 6, 1, 0, 0));
        r.setPeriodoFin(LocalDateTime.of(2026, 6, 30, 23, 59));
        r.setGeneradoPor("admin");
        r.setSucursalId(1L);
        r.setCantidadRegistros(150);
        return r;
    }
}
