package com.mymarket.ms_inventario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_inventario.model.EstadoInventario;
import com.mymarket.ms_inventario.model.InventarioModel;
import com.mymarket.ms_inventario.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class InventarioControllerTest {

    @Mock
    private InventarioService service;

    @InjectMocks
    private InventarioController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarTodos_debeRetornar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001")));

        mockMvc.perform(get("/api/v1/inventario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lote").value("LOTE-001"));
    }

    @Test
    void buscarPorId_debeRetornar200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(inventarioDePrueba(1L, "LOTE-001"));

        mockMvc.perform(get("/api/v1/inventario/1"))
                .andExpect(status().isOk());
    }

    @Test
    void registrarEntrada_debeRetornar200() throws Exception {
        when(service.registrarEntrada(eq(1L), eq(50)))
                .thenReturn(inventarioDePrueba(1L, "LOTE-001"));

        mockMvc.perform(put("/api/v1/inventario/1/entrada").param("cantidad", "50"))
                .andExpect(status().isOk());
    }

    @Test
    void registrarSalida_debeRetornar200() throws Exception {
        when(service.registrarSalida(eq(1L), eq(30)))
                .thenReturn(inventarioDePrueba(1L, "LOTE-001"));

        mockMvc.perform(put("/api/v1/inventario/1/salida").param("cantidad", "30"))
                .andExpect(status().isOk());
    }

    @Test
    void registrarMerma_debeRetornar200() throws Exception {
        when(service.registrarMerma(eq(1L), eq(10)))
                .thenReturn(inventarioDePrueba(1L, "LOTE-001"));

        mockMvc.perform(put("/api/v1/inventario/1/merma").param("cantidad", "10"))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarLogico_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/v1/inventario/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorProductoId_debeRetornar200() throws Exception {
        when(service.buscarPorProductoId(10L))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001")));

        mockMvc.perform(get("/api/v1/inventario/producto/10"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorLote_debeRetornar200() throws Exception {
        when(service.buscarPorLote("LOTE-001"))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001")));

        mockMvc.perform(get("/api/v1/inventario/lote/LOTE-001"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorEstado_debeRetornar200() throws Exception {
        when(service.buscarPorEstado(EstadoInventario.BAJO_STOCK))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001")));

        mockMvc.perform(get("/api/v1/inventario/estado/BAJO_STOCK"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorActivo_debeRetornar200() throws Exception {
        when(service.buscarPorActivo(true))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001")));

        mockMvc.perform(get("/api/v1/inventario/activo/true"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarVencidos_debeRetornar200() throws Exception {
        when(service.buscarVencidos()).thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001")));

        mockMvc.perform(get("/api/v1/inventario/vencidos"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorVencer_debeRetornar200() throws Exception {
        when(service.buscarPorVencer(any(Integer.class)))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001")));

        mockMvc.perform(get("/api/v1/inventario/por-vencer").param("dias", "7"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarStockBajo_debeRetornar200() throws Exception {
        when(service.buscarStockBajo(20))
                .thenReturn(List.of(inventarioDePrueba(1L, "LOTE-001")));

        mockMvc.perform(get("/api/v1/inventario/stock-bajo").param("stockLimite", "20"))
                .andExpect(status().isOk());
    }

    private InventarioModel inventarioDePrueba(Long id, String lote) {
        return InventarioModel.builder()
                .id(id)
                .productoId(10L)
                .lote(lote)
                .stockActual(100)
                .stockMinimo(10)
                .stockMaximo(500)
                .merma(0)
                .fechaIngreso(LocalDate.of(2026, 1, 15))
                .fechaVencimiento(LocalDate.of(2027, 6, 1))
                .estado(EstadoInventario.DISPONIBLE)
                .activo(true)
                .build();
    }
}
