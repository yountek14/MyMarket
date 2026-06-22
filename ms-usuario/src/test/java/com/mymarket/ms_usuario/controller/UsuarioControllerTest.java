package com.mymarket.ms_usuario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_usuario.enums.Rol;
import com.mymarket.ms_usuario.model.Usuario;
import com.mymarket.ms_usuario.service.UsuarioService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService service;

    @InjectMocks
    private UsuarioController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarTodos_debeRetornar200() throws Exception {
        when(service.listarTodos()).thenReturn(List.of(usuarioDePrueba(1L)));

        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("juan@test.cl"));
    }

    @Test
    void buscarPorId_debeRetornar200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(usuarioDePrueba(1L));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isOk());
    }

    @Test
    void crear_debeRetornar201() throws Exception {
        Usuario u = usuarioDePrueba(null);
        Usuario guardado = usuarioDePrueba(1L);
        when(service.crear(any(Usuario.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_debeRetornar200() throws Exception {
        Usuario datos = usuarioDePrueba(null);
        datos.setNombre("Juan Perez");
        when(service.actualizar(eq(1L), any(Usuario.class))).thenReturn(usuarioDePrueba(1L));

        mockMvc.perform(put("/api/usuarios/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(datos)))
                .andExpect(status().isOk());
    }

    @Test
    void desactivar_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/usuarios/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void crear_conDatosInvalidos_debeRetornar400() throws Exception {
        Usuario u = new Usuario();

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(u)))
                .andExpect(status().isBadRequest());
    }

    private Usuario usuarioDePrueba(Long id) {
        Usuario u = new Usuario();
        u.setId(id);
        u.setNombre("Juan");
        u.setEmail("juan@test.cl");
        u.setPassword("password123");
        u.setRol(Rol.ADMIN);
        u.setActivo(true);
        return u;
    }
}
