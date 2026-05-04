package MyMarket.ms_precios.repository;

import MyMarket.ms_precios.model.Precio;
import MyMarket.ms_precios.model.Temporada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Long> {
    List<Precio> findByProductoId(Long productoId);
    List<Precio> findByProductoIdAndActivoTrue(Long productoId);
    List<Precio> findByTemporada(Temporada temporada);
    List<Precio> findByActivoTrue();
    Optional<Precio> findTopByProductoIdAndActivoTrueOrderByFechaInicioDesc(Long productoId);
}
