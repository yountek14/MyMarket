package com.mymarket.ms_pedidos.service;

import com.mymarket.ms_pedidos.model.Pedido;
import com.mymarket.ms_pedidos.repository.PedidoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoServiceImpl implements IPedidoService {

    @Autowired
    private PedidoRepo repo;

    @Override
    public List<Pedido> listarTodos() {
        return repo.findAll();
    }

    @Override
    public Pedido buscarPorId(Long id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        // Si no viene fecha, se asigna la fecha de hoy automáticamente
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDate.now());
        }
        // Si no viene estado, se asigna PENDIENTE por defecto
        if (pedido.getEstado() == null) {
            pedido.setEstado("PENDIENTE");
        }
        // El estado siempre se guarda en mayúsculas
        pedido.setEstado(pedido.getEstado().toUpperCase());
        return repo.save(pedido);
    }

    @Override
    public void eliminar(Long id) {
        repo.deleteById(id);
    }

    @Override
    public List<Pedido> buscarPorEstado(String estado) {
        return repo.findByEstado(estado.toUpperCase());
    }

    @Override
    public List<Pedido> buscarPorProveedor(Long proveedorId) {
        return repo.findByProveedorId(proveedorId);
    }
}