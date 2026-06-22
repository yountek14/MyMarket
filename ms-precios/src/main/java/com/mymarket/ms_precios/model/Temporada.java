package com.mymarket.ms_precios.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Temporada del ano aplicable al precio")
public enum Temporada {
    VERANO, OTOÑO, PRIMAVERA, INVIERNO
}
