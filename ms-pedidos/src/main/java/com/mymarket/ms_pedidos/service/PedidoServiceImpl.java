package com.mymarket.ms_pedidos.service;

import com.mymarket.ms_pedidos.model.Pedido;
import com.mymarket.ms_pedidos.repository.PedidoRepo;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class PedidoServiceImpl implements IPedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    private final PedidoRepo repo;

    public PedidoServiceImpl(PedidoRepo repo) {
        this.repo = repo;
    }

    @Override
    public List<Pedido> listarTodos() {
        return repo.findAll();
    }

    @Override
    public Pedido buscarPorId(Long id) {
        log.info("Buscando pedido por id: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pedido no encontrado con id: {}", id);
                    return new EntityNotFoundException("Pedido no encontrado con id: " + id);
                });
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDate.now());
        }
        if (pedido.getEstado() == null) {
            pedido.setEstado("PENDIENTE");
        }
        pedido.setEstado(pedido.getEstado().toUpperCase());
        Pedido guardado = repo.save(pedido);
        log.info("Pedido creado con id: {}, estado: {}", guardado.getId(), guardado.getEstado());
        return guardado;
    }

    @Override
    public void eliminar(Long id) {
        Pedido pedido = buscarPorId(id);
        repo.deleteById(pedido.getId());
        log.info("Pedido eliminado con id: {}", id);
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