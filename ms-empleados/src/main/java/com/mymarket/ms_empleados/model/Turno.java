package com.mymarket.ms_empleados.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Turno laboral del empleado")
public enum Turno {
    MAÑANA, TARDE, NOCHE
}
