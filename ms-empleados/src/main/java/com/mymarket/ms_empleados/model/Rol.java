package com.mymarket.ms_empleados.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Rol del empleado en el sistema")
public enum Rol {
    ADMIN, CAJERO, BODEGERO
}
