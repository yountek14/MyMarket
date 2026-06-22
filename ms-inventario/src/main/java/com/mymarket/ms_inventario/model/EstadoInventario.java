package com.mymarket.ms_inventario.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado del inventario: disponible, bajo stock, agotado o vencido")
public enum EstadoInventario {
    DISPONIBLE,
    BAJO_STOCK,
    AGOTADO,
    VENCIDO
}