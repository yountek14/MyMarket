package com.mymarket.ms_pedidos.service;
import com.mymarket.ms_pedidos.model.Pedido;
import java.util.List;

public interface IPedidoService {
    List<Pedido> listarTodos();
    Pedido buscarPorId(Long id);
    Pedido guardar(Pedido pedido);
    void eliminar(Long id);
    List<Pedido> buscarPorEstado(String estado);
    List<Pedido> buscarPorProveedor(Long proveedorId);
}