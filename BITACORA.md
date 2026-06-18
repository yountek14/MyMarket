# Bitacora de Cambios - MyMarket

## 2026-06-09 - Correccion de error WebClient.Builder

### Problema
Al iniciar los microservicios ms-inventario y ms-ventas, se producia el siguiente error:
```
Action: Consider defining a bean of type 'org.springframework.web.reactive.function.client.WebClient$Builder' in your configuration.
```

### Causa
Los microservicios `ms-inventario` y `ms-ventas` inyectan `WebClient.Builder` por constructor en sus servicios (`InventarioService` y `VentaService`) para realizar llamadas HTTP a otros microservicios, pero no tenian un bean `WebClient.Builder` definido explicitamente en su configuracion.

Si bien ambos incluyen la dependencia `spring-boot-starter-webflux` en su `pom.xml`, al coexistir tambien con `spring-boot-starter-webmvc`, Spring Boot prioriza el stack servlet y en ocasiones la auto-configuracion del `WebClient.Builder` del lado reactivo no se activa correctamente.

El microservicio `ms-alertas` si funcionaba porque ya contaba con el `@Bean` explicito en su clase principal `MsAlertasApplication`.

### Archivos modificados

1. **ms-inventario/src/main/java/com/mymarket/ms_inventario/MsInventarioApplication.java**
   - Agregado `@Bean` que retorna `WebClient.builder()`
   - Agregados imports correspondientes (`@Bean`, `WebClient`)

2. **ms-ventas/src/main/java/com/mymarket/ms_ventas/MsVentasApplication.java**
   - Agregado `@Bean` que retorna `WebClient.builder()`
   - Agregados imports correspondientes (`@Bean`, `WebClient`)

### Microservicios que usan WebClient (comunicacion entre MS)

| Microservicio | Llama a              | Puerto destino |
|---------------|----------------------|----------------|
| ms-alertas    | ms-inventario        | 8083           |
| ms-alertas    | ms-productos         | 8087           |
| ms-ventas     | ms-productos         | 8087           |
| ms-ventas     | ms-inventario        | 8083           |
| ms-inventario | ms-productos         | 8087           |
