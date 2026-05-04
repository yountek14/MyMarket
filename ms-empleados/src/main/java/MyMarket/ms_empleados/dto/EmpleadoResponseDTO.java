package MyMarket.ms_empleados.dto;

import MyMarket.ms_empleados.model.Rol;
import MyMarket.ms_empleados.model.Turno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String rut;
    private String telefono;
    private Rol rol;
    private Turno turno;
    private LocalDate fechaContratacion;
    private boolean activo;
    private Long usuarioId;
}
