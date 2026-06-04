# MyMarket — DSY1103 Desarrollo FullStack I

## Descripción

MyMarket es un sistema basado en arquitectura de microservicios orientado a la gestión de un minimarket.  
El proyecto permite administrar productos, inventario, ventas, proveedores, empleados, usuarios, pedidos, precios, alertas y reportes.

El sistema busca resolver problemas comunes de administración en tiendas, tales como el control de stock, productos próximos a vencer, registro de ventas, gestión de proveedores y generación de información útil para la toma de decisiones.

---
## Equipo

| Nombre          | GitHub       |
|-----------------|--------------|
| Benjamin Aguero | yountek14    |
| Ignacio Salazar | Shir3n       |
| Luciano Garrido | BlackCatVSC  |

---

## Microservicios Implementados

| # | Microservicio | Puerto | Descripción |
|---|--------------|--------|-------------|
| 1 | ms-productos | 8087   | Gestiona el catálogo de productos, incluyendo nombre, categoría, unidad de medida, precio base, descripción y fecha de caducidad. |
| 2 | ms-inventario | 8083   | Controla stock, lotes, fechas de vencimiento, merma, stock mínimo y stock máximo. Se integra con ms-productos mediante DTO. |
| 3 | ms-ventas | 8084   | Registra ventas, valida productos, descuenta stock desde inventario y calcula el total de la venta. |
| 4 | ms-alertas | 8085   | Genera alertas por stock bajo, stock agotado, productos vencidos o próximos a vencer. |
| 5 | ms-proveedor | 8080   | Gestiona proveedores, información de contacto y datos asociados al abastecimiento. |
| 6 | ms-usuarios | 8088   | Gestiona usuarios del sistema, roles y datos de acceso. |
| 7 | ms-empleados | 8082   | Administra información del personal, cargos y datos asociados a trabajadores. |
| 8 | ms-pedidos | 8081   | Gestiona órdenes o solicitudes de productos a proveedores. |
| 9 | ms-precios | 8086   | Administra precios, variaciones, descuentos e historial de precios. |
| 10 | ms-reportes | 8089   | Consolida información de ventas, inventario, productos y proveedores para generar reportes. |

> Nota: Los puertos pueden ajustarse según el archivo `application.properties` de cada microservicio.

---

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.x / 4.x
- Spring Web
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- Lombok
- Jakarta Validation
- DTO para comunicación entre microservicios
- ControllerAdvice para manejo centralizado de errores
- Postman para pruebas de endpoints
- Git y GitHub para control de versiones

---

## Arquitectura del Proyecto

El proyecto utiliza una arquitectura basada en microservicios.  
Cada microservicio es independiente y cuenta con su propia estructura interna:

```text
controller
model
repository
service
dto
exception