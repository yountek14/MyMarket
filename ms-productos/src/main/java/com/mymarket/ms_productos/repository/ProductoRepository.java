package com.mymarket.ms_productos.repository;

import com.mymarket.ms_productos.model.ProductoModel;
import com.mymarket.ms_productos.model.UnidadMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<ProductoModel, Long> {

    List<ProductoModel> findByCategoria(String categoria);

    List<ProductoModel> findByActivo(Boolean activo);

    List<ProductoModel> findByNombreProductoContainingIgnoreCase(String nombreProducto);

    List<ProductoModel> findByUnidadMedida(UnidadMedida unidadMedida);

    Optional<ProductoModel> findByNombreProductoIgnoreCase(String nombreProducto);

    boolean existsByNombreProductoIgnoreCase(String nombreProducto);
}