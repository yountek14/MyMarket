package com.mymarket.ms_precios.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Tipo de descuento: porcentaje o fijo")
public enum TipoDescuento {
    PORCENTAJE, FIJO
}
