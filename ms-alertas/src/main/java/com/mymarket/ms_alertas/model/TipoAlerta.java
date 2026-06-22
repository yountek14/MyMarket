package com.mymarket.ms_alertas.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de alerta: stock bajo, agotado, vencido o por vencer")
public enum TipoAlerta {
    STOCK_BAJO,
    STOCK_AGOTADO,
    PRODUCTO_VENCIDO,
    PRODUCTO_POR_VENCER
}