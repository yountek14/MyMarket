package com.mymarket.ms_productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_productos.model.ProductoModel;
import com.mymarket.ms_productos.model.UnidadMedida;
import com.mymarket.ms_productos.service.ProductoService;
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
class ProductoControllerTest {

    @Mock
    private ProductoService service;

    @InjectMocks
    private ProductoController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarTodos_debeRetornar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(productoDePrueba(1L, "Leche")));

        mockMvc.perform(get("/api/v1/productos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreProducto").value("Leche"));
    }

    @Test
    void buscarPorId_debeRetornar200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(productoDePrueba(1L, "Leche"));

        mockMvc.perform(get("/api/v1/productos/1"))
                .andExpect(status().isOk());
    }

    @Test
    void guardar_debeRetornar201() throws Exception {
        ProductoModel p = productoDePrueba(null, "Leche");
        ProductoModel guardado = productoDePrueba(1L, "Leche");
        when(service.guardar(any(ProductoModel.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_debeRetornar200() throws Exception {
        ProductoModel datos = productoDePrueba(null, "Leche Entera");
        when(service.actualizar(eq(1L), any(ProductoModel.class)))
                .thenReturn(productoDePrueba(1L, "Leche Entera"));

        mockMvc.perform(put("/api/v1/productos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk());
    }

    @Test
    void eliminarLogico_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/v1/productos/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPorCategoria_debeRetornar200() throws Exception {
        when(service.buscarPorCategoria("Lacteos"))
                .thenReturn(List.of(productoDePrueba(1L, "Leche")));

        mockMvc.perform(get("/api/v1/productos/categoria/Lacteos"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorActivo_debeRetornar200() throws Exception {
        when(service.buscarPorActivo(true))
                .thenReturn(List.of(productoDePrueba(1L, "Leche")));

        mockMvc.perform(get("/api/v1/productos/activo/true"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorNombre_debeRetornar200() throws Exception {
        when(service.buscarPorNombre("lec"))
                .thenReturn(List.of(productoDePrueba(1L, "Leche")));

        mockMvc.perform(get("/api/v1/productos/buscar").param("nombreProducto", "lec"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorUnidadMedida_debeRetornar200() throws Exception {
        when(service.buscarPorUnidadMedida(UnidadMedida.UNIDAD))
                .thenReturn(List.of(productoDePrueba(1L, "Mouse")));

        mockMvc.perform(get("/api/v1/productos/unidad/UNIDAD"))
                .andExpect(status().isOk());
    }

    @Test
    void guardar_conDatosInvalidos_debeRetornar400() throws Exception {
        ProductoModel p = new ProductoModel();

        mockMvc.perform(post("/api/v1/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isBadRequest());
    }

    private ProductoModel productoDePrueba(Long id, String nombre) {
        return ProductoModel.builder()
                .id(id)
                .nombreProducto(nombre)
                .categoria("Lacteos")
                .unidadMedida(UnidadMedida.UNIDAD)
                .precioBase(1000.0)
                .activo(true)
                .descripcionProducto("Desc")
                .fechaCaducidad(LocalDate.of(2027, 6, 1))
                .build();
    }
}
