package com.mymarket.ms_proveedores.repository;

import com.mymarket.ms_proveedores.model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProveedorRepo  extends JpaRepository<Proveedor,Long> {
    Optional<Proveedor> findByRut(String rut);
}
