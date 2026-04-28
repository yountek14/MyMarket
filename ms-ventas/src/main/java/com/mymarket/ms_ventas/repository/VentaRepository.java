package com.mymarket.ms_ventas.repository;

import com.mymarket.ms_ventas.model.EstadoVenta;
import com.mymarket.ms_ventas.model.VentaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VentaRepository extends JpaRepository<VentaModel, Long> {

    List<VentaModel> findByProductoId(Long productoId);

    List<VentaModel> findByInventarioId(Long inventarioId);

    List<VentaModel> findByEstado(EstadoVenta estado);

    List<VentaModel> findByActivo(Boolean activo);

    List<VentaModel> findByFechaVentaBetween(LocalDateTime inicio, LocalDateTime fin);

}