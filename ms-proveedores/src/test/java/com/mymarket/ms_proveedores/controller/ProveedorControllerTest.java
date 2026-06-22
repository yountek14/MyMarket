package com.mymarket.ms_proveedores.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_proveedores.model.Proveedor;
import com.mymarket.ms_proveedores.service.IProveedorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProveedorControllerTest {

    @Mock
    private IProveedorService service;

    @InjectMocks
    private ProveedorController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarTodos_debeRetornar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(proveedorDePrueba(1L, "Prov A")));

        mockMvc.perform(get("/api/proveedores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Prov A"));
    }

    @Test
    void buscarPorId_debeRetornar200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(proveedorDePrueba(1L, "Prov A"));

        mockMvc.perform(get("/api/proveedores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorRut_debeRetornar200() throws Exception {
        when(service.buscarPorRut("11.111.111-1"))
                .thenReturn(proveedorDePrueba(1L, "Prov A"));

        mockMvc.perform(get("/api/proveedores/rut/11.111.111-1"))
                .andExpect(status().isOk());
    }

    @Test
    void guardar_debeRetornar201() throws Exception {
        Proveedor p = proveedorDePrueba(null, "Prov A");
        Proveedor guardado = proveedorDePrueba(1L, "PROV A");
        when(service.guardar(any(Proveedor.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/proveedores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(p)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void eliminar_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/proveedores/1"))
                .andExpect(status().isNoContent());
    }

    private Proveedor proveedorDePrueba(Long id, String nombre) {
        Proveedor p = new Proveedor();
        p.setId(id);
        p.setNombre(nombre);
        p.setRut("11.111.111-1");
        p.setDireccion("Av. Test 123");
        p.setTelefono("+56212345678");
        p.setEmail("test@test.cl");
        p.setRubro("Tecnologia");
        return p;
    }
}
