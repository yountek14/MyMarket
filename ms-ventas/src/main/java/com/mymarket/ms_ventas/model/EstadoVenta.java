package com.mymarket.ms_ventas.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado de la venta: registrada, pagada o anulada")
public enum EstadoVenta {
    REGISTRADA,
    PAGADA,
    ANULADA
}