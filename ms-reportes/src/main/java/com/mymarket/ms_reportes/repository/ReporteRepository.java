package com.mymarket.ms_reportes.repository;

import com.mymarket.ms_reportes.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {
    // Al heredar de JpaRepository, ya tenemos todo el CRUD listo.
}
