package MyMarket.ms_empleados.dto;

import MyMarket.ms_empleados.model.Rol;
import MyMarket.ms_empleados.model.Turno;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpleadoRequestDTO {
    @NotBlank
    private String nombre;

    @NotBlank
    private String apellido;

    @NotBlank
    private String rut;

    @NotBlank
    private String telefono;

    @NotNull
    private Rol rol;

    @NotNull
    private Turno turno;

    @NotNull
    private LocalDate fechaContratacion;

    @NotNull
    private Long usuarioId;

}
