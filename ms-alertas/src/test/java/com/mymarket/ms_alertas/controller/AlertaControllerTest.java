package com.mymarket.ms_alertas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_alertas.model.AlertaModel;
import com.mymarket.ms_alertas.model.EstadoAlerta;
import com.mymarket.ms_alertas.model.TipoAlerta;
import com.mymarket.ms_alertas.service.AlertaService;
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
class AlertaControllerTest {

    @Mock
    private AlertaService service;

    @InjectMocks
    private AlertaController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarTodas_debeRetornar200() throws Exception {
        when(service.listarTodas())
                .thenReturn(List.of(alertaDePrueba(1L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA)));

        mockMvc.perform(get("/api/v1/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tipoAlerta").value("STOCK_BAJO"));
    }

    @Test
    void buscarPorId_debeRetornar200() throws Exception {
        when(service.buscarPorId(1L))
                .thenReturn(alertaDePrueba(1L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA));

        mockMvc.perform(get("/api/v1/alertas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void crearAlertaManual_debeRetornar201() throws Exception {
        AlertaModel a = alertaDePrueba(null, TipoAlerta.STOCK_BAJO, EstadoAlerta.PENDIENTE);
        when(service.crearAlertaManual(any(AlertaModel.class)))
                .thenReturn(alertaDePrueba(1L, TipoAlerta.STOCK_BAJO, EstadoAlerta.PENDIENTE));

        mockMvc.perform(post("/api/v1/alertas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(a)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void resolverAlerta_debeRetornar200() throws Exception {
        when(service.resolverAlerta(1L))
                .thenReturn(alertaDePrueba(1L, TipoAlerta.STOCK_BAJO, EstadoAlerta.RESUELTA));

        mockMvc.perform(put("/api/v1/alertas/1/resolver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoAlerta").value("RESUELTA"));
    }

    @Test
    void eliminarLogico_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/v1/alertas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorProductoId_debeRetornar200() throws Exception {
        when(service.buscarPorProductoId(10L))
                .thenReturn(List.of(alertaDePrueba(1L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA)));

        mockMvc.perform(get("/api/v1/alertas/producto/10"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorInventarioId_debeRetornar200() throws Exception {
        when(service.buscarPorInventarioId(5L))
                .thenReturn(List.of(alertaDePrueba(1L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA)));

        mockMvc.perform(get("/api/v1/alertas/inventario/5"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorTipo_debeRetornar200() throws Exception {
        when(service.buscarPorTipo(TipoAlerta.PRODUCTO_VENCIDO))
                .thenReturn(List.of(alertaDePrueba(1L, TipoAlerta.PRODUCTO_VENCIDO, EstadoAlerta.PENDIENTE)));

        mockMvc.perform(get("/api/v1/alertas/tipo/PRODUCTO_VENCIDO"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorEstado_debeRetornar200() throws Exception {
        when(service.buscarPorEstado(EstadoAlerta.RESUELTA))
                .thenReturn(List.of(alertaDePrueba(1L, TipoAlerta.STOCK_AGOTADO, EstadoAlerta.RESUELTA)));

        mockMvc.perform(get("/api/v1/alertas/estado/RESUELTA"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorActivo_debeRetornar200() throws Exception {
        when(service.buscarPorActivo(true))
                .thenReturn(List.of(alertaDePrueba(1L, TipoAlerta.STOCK_BAJO, EstadoAlerta.ACTIVA)));

        mockMvc.perform(get("/api/v1/alertas/activo/true"))
                .andExpect(status().isOk());
    }

    private AlertaModel alertaDePrueba(Long id, TipoAlerta tipo, EstadoAlerta estado) {
        return AlertaModel.builder()
                .id(id)
                .productoId(10L)
                .inventarioId(5L)
                .tipoAlerta(tipo)
                .estadoAlerta(estado)
                .mensaje("Mensaje de prueba")
                .fechaCreacion(LocalDateTime.of(2026, 6, 15, 10, 0))
                .activo(true)
                .build();
    }
}
