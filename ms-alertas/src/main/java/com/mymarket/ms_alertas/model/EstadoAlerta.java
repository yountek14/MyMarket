package com.mymarket.ms_alertas.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estado de la alerta: pendiente, activa o resuelta")
public enum EstadoAlerta {

    // La alerta fue generada y aún no ha sido revisada o atendida.
    // Ejemplo: se detectó bajo stock, pero todavía nadie ha tomado acción.
    PENDIENTE,

    // La alerta está vigente y requiere atención.
    // Ejemplo: producto con stock crítico o próximo a vencer.
    ACTIVA,

    // La alerta ya fue atendida o solucionada.
    // Ejemplo: se repuso stock o se retiró un producto vencido.
    RESUELTA
}