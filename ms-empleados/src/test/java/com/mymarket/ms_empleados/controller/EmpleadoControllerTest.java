package com.mymarket.ms_empleados.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mymarket.ms_empleados.dto.EmpleadoRequestDTO;
import com.mymarket.ms_empleados.dto.EmpleadoResponseDTO;
import com.mymarket.ms_empleados.model.Rol;
import com.mymarket.ms_empleados.model.Turno;
import com.mymarket.ms_empleados.service.EmpleadoService;
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
class EmpleadoControllerTest {

    @Mock
    private EmpleadoService service;

    @InjectMocks
    private EmpleadoController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void listarTodos_debeRetornar200YLista() throws Exception {
        EmpleadoResponseDTO dto = responseDePrueba(1L, "Ana");
        when(service.listarTodos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/empleados"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }

    @Test
    void buscarPorId_debeRetornar200() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(responseDePrueba(1L, "Ana"));

        mockMvc.perform(get("/api/empleados/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana"));
    }

    @Test
    void crear_debeRetornar201() throws Exception {
        EmpleadoRequestDTO request = requestDePrueba();
        EmpleadoResponseDTO response = responseDePrueba(1L, "Ana");
        when(service.crear(any(EmpleadoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void actualizar_debeRetornar200() throws Exception {
        EmpleadoRequestDTO request = requestDePrueba();
        request.setNombre("Mariana");
        when(service.actualizar(eq(1L), any(EmpleadoRequestDTO.class)))
                .thenReturn(responseDePrueba(1L, "Mariana"));

        mockMvc.perform(put("/api/empleados/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Mariana"));
    }

    @Test
    void desactivar_debeRetornar204() throws Exception {
        mockMvc.perform(delete("/api/empleados/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void listarActivos_debeRetornar200() throws Exception {
        when(service.listarActivos()).thenReturn(List.of(responseDePrueba(1L, "Ana")));

        mockMvc.perform(get("/api/empleados/activos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].activo").value(true));
    }

    @Test
    void listarPorRol_debeRetornar200() throws Exception {
        when(service.listarPorRol(Rol.CAJERO))
                .thenReturn(List.of(responseDePrueba(1L, "Ana")));

        mockMvc.perform(get("/api/empleados/rol/CAJERO"))
                .andExpect(status().isOk());
    }

    @Test
    void listarPorTurno_debeRetornar200() throws Exception {
        when(service.listarPorTurno(Turno.MAÑANA))
                .thenReturn(List.of(responseDePrueba(1L, "Ana")));

        mockMvc.perform(get("/api/empleados/turno/MAÑANA"))
                .andExpect(status().isOk());
    }

    @Test
    void buscarPorUsuarioId_debeRetornar200() throws Exception {
        when(service.buscarPorUsuarioId(10L))
                .thenReturn(responseDePrueba(1L, "Ana"));

        mockMvc.perform(get("/api/empleados/usuario/10"))
                .andExpect(status().isOk());
    }

    @Test
    void crear_conDatosInvalidos_debeRetornar400() throws Exception {
        EmpleadoRequestDTO request = new EmpleadoRequestDTO();

        mockMvc.perform(post("/api/empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private EmpleadoResponseDTO responseDePrueba(Long id, String nombre) {
        EmpleadoResponseDTO dto = new EmpleadoResponseDTO();
        dto.setId(id);
        dto.setNombre(nombre);
        dto.setApellido("Test");
        dto.setRut("11.111.111-1");
        dto.setTelefono("+56912345678");
        dto.setRol(Rol.CAJERO);
        dto.setTurno(Turno.MAÑANA);
        dto.setFechaContratacion(LocalDate.of(2026, 1, 15));
        dto.setActivo(true);
        dto.setUsuarioId(10L);
        return dto;
    }

    private EmpleadoRequestDTO requestDePrueba() {
        EmpleadoRequestDTO dto = new EmpleadoRequestDTO();
        dto.setNombre("Ana");
        dto.setApellido("Test");
        dto.setRut("11.111.111-1");
        dto.setTelefono("+56912345678");
        dto.setRol(Rol.CAJERO);
        dto.setTurno(Turno.MAÑANA);
        dto.setFechaContratacion(LocalDate.of(2026, 1, 15));
        dto.setUsuarioId(10L);
        return dto;
    }
}
