package com.mymarket.ms_inventario.repository;

import com.mymarket.ms_inventario.model.EstadoInventario;
import com.mymarket.ms_inventario.model.InventarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InventarioRepository extends JpaRepository<InventarioModel, Long> {

    List<InventarioModel> findByProductoId(Long productoId);

    List<InventarioModel> findByLote(String lote);

    List<InventarioModel> findByEstado(EstadoInventario estado);

    List<InventarioModel> findByActivo(Boolean activo);

    List<InventarioModel> findByFechaVencimientoBefore(LocalDate fecha);

    List<InventarioModel> findByFechaVencimientoBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<InventarioModel> findByStockActualLessThanEqual(Integer stockActual);

    boolean existsByLote(String lote);
}
