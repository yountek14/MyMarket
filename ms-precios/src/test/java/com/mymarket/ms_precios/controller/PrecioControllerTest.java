package com.mymarket.ms_precios.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_precios.dto.PrecioRequestDTO;
import com.mymarket.ms_precios.dto.PrecioResponseDTO;
import com.mymarket.ms_precios.model.Temporada;
import com.mymarket.ms_precios.service.PrecioService;
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
class PrecioControllerTest {

    @Mock
    private PrecioService service;

    @InjectMocks
    private PrecioController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarTodos_debeRetornar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(responseDePrueba(1L, 10L)));

        mockMvc.perform(get("/api/precios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productoId").value(10));
    }

    @Test
    void listarActivos_debeRetornar200() throws Exception {
        when(service.listarActivos()).thenReturn(List.of(responseDePrueba(1L, 10L)));

        mockMvc.perform(get("/api/precios/activos"))
                .andExpect(status().isOk());
    }

    @Test
    void listarPorProducto_debeRetornar200() throws Exception {
        when(service.listarPorProducto(10L))
                .thenReturn(List.of(responseDePrueba(1L, 10L)));

        mockMvc.perform(get("/api/precios/producto/10"))
                .andExpect(status().isOk());
    }

    @Test
    void precioActual_debeRetornar200() throws Exception {
        when(service.buscarPrecioActualDeProducto(10L))
                .thenReturn(responseDePrueba(1L, 10L));

        mockMvc.perform(get("/api/precios/producto/10/actual"))
                .andExpect(status().isOk());
    }

    @Test
    void listarPorTemporada_debeRetornar200() throws Exception {
        when(service.listarPorTemporada(Temporada.VERANO))
                .thenReturn(List.of(responseDePrueba(1L, 10L)));

        mockMvc.perform(get("/api/precios/temporada/VERANO"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorId_debeRetornar200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(responseDePrueba(1L, 10L));

        mockMvc.perform(get("/api/precios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void crear_debeRetornar201() throws Exception {
        PrecioRequestDTO request = requestDePrueba();
        when(service.crear(any(PrecioRequestDTO.class)))
                .thenReturn(responseDePrueba(1L, 10L));

        mockMvc.perform(post("/api/precios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_debeRetornar200() throws Exception {
        PrecioRequestDTO request = requestDePrueba();
        when(service.actualizar(eq(1L), any(PrecioRequestDTO.class)))
                .thenReturn(responseDePrueba(1L, 10L));

        mockMvc.perform(put("/api/precios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    void desactivar_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/precios/1"))
                .andExpect(status().isNoContent());
    }

    private PrecioResponseDTO responseDePrueba(Long id, Long productoId) {
        PrecioResponseDTO dto = new PrecioResponseDTO();
        dto.setId(id);
        dto.setProductoId(productoId);
        dto.setPrecioBase(1000.0);
        dto.setPrecioFinal(900.0);
        dto.setTemporada(Temporada.VERANO);
        dto.setFechaInicio(LocalDate.of(2026, 1, 1));
        dto.setFechaFin(LocalDate.of(2026, 3, 31));
        dto.setActivo(true);
        return dto;
    }

    private PrecioRequestDTO requestDePrueba() {
        PrecioRequestDTO dto = new PrecioRequestDTO();
        dto.setProductoId(10L);
        dto.setPrecioBase(1000.0);
        dto.setTemporada(Temporada.VERANO);
        dto.setFechaInicio(LocalDate.of(2026, 1, 1));
        dto.setFechaFin(LocalDate.of(2026, 3, 31));
        return dto;
    }
}
