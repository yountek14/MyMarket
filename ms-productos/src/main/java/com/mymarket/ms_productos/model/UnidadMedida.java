package com.mymarket.ms_productos.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Unidad de medida del producto")
public enum UnidadMedida {
    KG,
    UNIDAD,
    CAJA
}