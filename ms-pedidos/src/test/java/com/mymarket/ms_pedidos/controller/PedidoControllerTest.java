package com.mymarket.ms_pedidos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_pedidos.model.Pedido;
import com.mymarket.ms_pedidos.service.IPedidoService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PedidoControllerTest {

    @Mock
    private IPedidoService service;

    @InjectMocks
    private PedidoController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarTodos_debeRetornar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(pedidoDePrueba(1L)));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void buscarPorId_debeRetornar200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(pedidoDePrueba(1L));

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorEstado_debeRetornar200() throws Exception {
        when(service.buscarPorEstado("PENDIENTE"))
                .thenReturn(List.of(pedidoDePrueba(1L)));

        mockMvc.perform(get("/api/pedidos/estado/PENDIENTE"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorProveedor_debeRetornar200() throws Exception {
        when(service.buscarPorProveedor(5L))
                .thenReturn(List.of(pedidoDePrueba(1L)));

        mockMvc.perform(get("/api/pedidos/proveedor/5"))
                .andExpect(status().isOk());
    }

    @Test
    void guardar_debeRetornar201() throws Exception {
        Pedido p = pedidoDePrueba(null);
        Pedido guardado = pedidoDePrueba(1L);
        when(service.guardar(any(Pedido.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/pedidos/1"))
                .andExpect(status().isNoContent());
    }

    private Pedido pedidoDePrueba(Long id) {
        Pedido p = new Pedido();
        p.setId(id);
        p.setFechaPedido(LocalDate.of(2026, 6, 1));
        p.setEstado("PENDIENTE");
        p.setTotal(150000.0);
        p.setProveedorId(5L);
        p.setUsuarioId(1L);
        p.setEmpleadoId(1L);
        p.setObservacion("Urgente");
        return p;
    }
}
