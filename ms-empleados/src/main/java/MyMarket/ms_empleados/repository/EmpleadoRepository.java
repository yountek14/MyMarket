package MyMarket.ms_empleados.repository;

import MyMarket.ms_empleados.model.Empleado;
import MyMarket.ms_empleados.model.Rol;
import MyMarket.ms_empleados.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    List<Empleado> findByActivo(boolean activo);
    List<Empleado> findByRol(Rol rol);
    List<Empleado> findByTurno(Turno turno);
    Optional<Empleado> findByRut(String rut);
    Optional<Empleado> findByUsuarioId(Long usuarioId);
}
