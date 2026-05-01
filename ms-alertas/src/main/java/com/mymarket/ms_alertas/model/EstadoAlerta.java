package com.mymarket.ms_alertas.model;

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