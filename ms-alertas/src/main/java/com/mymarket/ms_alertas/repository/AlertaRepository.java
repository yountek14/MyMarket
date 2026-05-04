package com.mymarket.ms_alertas.repository;

import com.mymarket.ms_alertas.model.AlertaModel;
import com.mymarket.ms_alertas.model.EstadoAlerta;
import com.mymarket.ms_alertas.model.TipoAlerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertaRepository extends JpaRepository<AlertaModel, Long> {

    List<AlertaModel> findByProductoId(Long productoId);

    List<AlertaModel> findByInventarioId(Long inventarioId);

    List<AlertaModel> findByTipoAlerta(TipoAlerta tipoAlerta);

    List<AlertaModel> findByEstadoAlerta(EstadoAlerta estadoAlerta);

    List<AlertaModel> findByActivo(Boolean activo);

    List<AlertaModel> findByProductoIdAndEstadoAlerta(Long productoId, EstadoAlerta estadoAlerta);

    boolean existsByInventarioIdAndTipoAlertaAndEstadoAlerta(
            Long inventarioId,
            TipoAlerta tipoAlerta,
            EstadoAlerta estadoAlerta
    );
}
