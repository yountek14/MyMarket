package com.mymarket.ms_ventas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_ventas.model.EstadoVenta;
import com.mymarket.ms_ventas.model.VentaModel;
import com.mymarket.ms_ventas.service.VentaService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VentaControllerTest {

    @Mock
    private VentaService service;

    @InjectMocks
    private VentaController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarTodos_debeRetornar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(ventaDePrueba(1L, EstadoVenta.REGISTRADA)));

        mockMvc.perform(get("/api/v1/ventas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].estado").value("REGISTRADA"));
    }

    @Test
    void buscarPorId_debeRetornar200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(ventaDePrueba(1L, EstadoVenta.REGISTRADA));

        mockMvc.perform(get("/api/v1/ventas/1"))
                .andExpect(status().isOk());
    }

    @Test
    void actualizar_debeRetornar200() throws Exception {
        VentaModel datos = ventaDePrueba(null, EstadoVenta.PAGADA);
        when(service.actualizar(eq(1L), any(VentaModel.class)))
                .thenReturn(ventaDePrueba(1L, EstadoVenta.PAGADA));

        mockMvc.perform(put("/api/v1/ventas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk());
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/v1/ventas/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void pagar_debeRetornar200() throws Exception {
        when(service.marcarComoPagada(1L))
                .thenReturn(ventaDePrueba(1L, EstadoVenta.PAGADA));

        mockMvc.perform(put("/api/v1/ventas/1/pagar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("PAGADA"));
    }

    @Test
    void anular_debeRetornar200() throws Exception {
        when(service.anularVenta(1L))
                .thenReturn(ventaDePrueba(1L, EstadoVenta.ANULADA));

        mockMvc.perform(put("/api/v1/ventas/1/anular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ANULADA"));
    }

    @Test
    void buscarPorProducto_debeRetornar200() throws Exception {
        when(service.buscarPorProductoId(10L))
                .thenReturn(List.of(ventaDePrueba(1L, EstadoVenta.REGISTRADA)));

        mockMvc.perform(get("/api/v1/ventas/producto/10"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorInventario_debeRetornar200() throws Exception {
        when(service.buscarPorInventarioId(5L))
                .thenReturn(List.of(ventaDePrueba(1L, EstadoVenta.REGISTRADA)));

        mockMvc.perform(get("/api/v1/ventas/inventario/5"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorEstado_debeRetornar200() throws Exception {
        when(service.buscarPorEstado(EstadoVenta.PAGADA))
                .thenReturn(List.of(ventaDePrueba(1L, EstadoVenta.PAGADA)));

        mockMvc.perform(get("/api/v1/ventas/estado/PAGADA"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorActivo_debeRetornar200() throws Exception {
        when(service.buscarPorActivo(true))
                .thenReturn(List.of(ventaDePrueba(1L, EstadoVenta.REGISTRADA)));

        mockMvc.perform(get("/api/v1/ventas/activo/true"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorFechas_debeRetornar200() throws Exception {
        LocalDateTime inicio = LocalDateTime.of(2026, 6, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2026, 6, 30, 23, 59);
        when(service.buscarPorRangoFechas(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(ventaDePrueba(1L, EstadoVenta.REGISTRADA)));

        mockMvc.perform(get("/api/v1/ventas/fechas")
                        .param("inicio", "2026-06-01T00:00:00")
                        .param("fin", "2026-06-30T23:59:00"))
                .andExpect(status().isOk());
    }

    private VentaModel ventaDePrueba(Long id, EstadoVenta estado) {
        return VentaModel.builder()
                .id(id)
                .productoId(10L)
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
