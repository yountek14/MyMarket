package com.mymarket.ms_pedidos.service;

import com.mymarket.ms_pedidos.model.Pedido;
import com.mymarket.ms_pedidos.repository.PedidoRepo;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private PedidoRepo repo;

    @InjectMocks
    private PedidoServiceImpl service;

    @Test
    void listarTodos_debeRetornarLista() {
        when(repo.findAll()).thenReturn(List.of(new Pedido(), new Pedido()));

        List<Pedido> resultado = service.listarTodos();

        assertEquals(2, resultado.size());
        verify(repo).findAll();
    }

    @Test
    void buscarPorId_cuandoExiste_debeRetornarPedido() {
        Pedido p = pedidoDePrueba(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Pedido resultado = service.buscarPorId(1L);

        assertEquals(1L, resultado.getId());
    }

    @Test
    void buscarPorId_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.buscarPorId(99L));
    }

    @Test
    void guardar_debeAsignarFechaPedidoSiEsNull() {
        Pedido p = new Pedido();
        p.setFechaPedido(null);
        p.setEstado(null);
        Pedido guardado = pedidoDePrueba(1L);
        when(repo.save(any(Pedido.class))).thenReturn(guardado);

        Pedido resultado = service.guardar(p);

        assertNotNull(resultado.getFechaPedido());
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    void guardar_debeConvertirEstadoAUppercase() {
        Pedido p = new Pedido();
        p.setEstado("entregado");
        Pedido guardado = pedidoDePrueba(1L);
        guardado.setEstado("ENTREGADO");
        when(repo.save(any(Pedido.class))).thenReturn(guardado);

        Pedido resultado = service.guardar(p);

        assertEquals("ENTREGADO", resultado.getEstado());
    }

    @Test
    void guardar_conFechaExistente_debeMantenerla() {
        Pedido p = new Pedido();
        p.setFechaPedido(LocalDate.of(2026, 3, 15));
        p.setEstado("pendiente");
        Pedido guardado = pedidoDePrueba(1L);
        guardado.setEstado("PENDIENTE");
        when(repo.save(any(Pedido.class))).thenReturn(guardado);

        Pedido resultado = service.guardar(p);

        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    void eliminar_debeEliminarPedidoExistente() {
        Pedido p = pedidoDePrueba(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        service.eliminar(1L);

        verify(repo).deleteById(1L);
    }

    @Test
    void eliminar_cuandoNoExiste_debeLanzarEntityNotFoundException() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> service.eliminar(99L));
    }

    @Test
    void buscarPorEstado_debeConvertirAUppercase() {
        when(repo.findByEstado("PENDIENTE"))
                .thenReturn(List.of(pedidoDePrueba(1L)));

        List<Pedido> resultado = service.buscarPorEstado("pendiente");

        assertEquals(1, resultado.size());
        verify(repo).findByEstado("PENDIENTE");
    }

    @Test
    void buscarPorProveedor_debeRetornarLista() {
        when(repo.findByProveedorId(5L))
                .thenReturn(List.of(pedidoDePrueba(1L)));

        List<Pedido> resultado = service.buscarPorProveedor(5L);

        assertEquals(1, resultado.size());
        verify(repo).findByProveedorId(5L);
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
