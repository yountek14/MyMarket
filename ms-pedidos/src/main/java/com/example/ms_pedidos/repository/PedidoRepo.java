package com.example.ms_pedidos.repository;
import com.example.ms_pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepo extends JpaRepository<Pedido, Long> {

    // Buscar todos los pedidos de un proveedor específico
    List<Pedido> findByProveedorId(Long proveedorId);

    // Buscar pedidos por estado (ej: todos los "PENDIENTE")
    List<Pedido> findByEstado(String estado);
}