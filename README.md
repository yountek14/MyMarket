# MyMarket — Sistema de Gestión para Minimarket

> **DSY1103 Desarrollo FullStack I — Evaluación Parcial 3 (25%)**
> Arquitectura de Microservicios con Spring Boot

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](#)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4%2F4.0-brightgreen.svg)](#)
[![Docker](https://img.shields.io/badge/Docker-✓-blue.svg)](#)
[![Tests](https://img.shields.io/badge/Tests-247%20passed-success.svg)](#)
[![Coverage](https://img.shields.io/badge/Coverage-≥80%25-brightgreen.svg)](#)
[![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D.svg)](#)
[![Render](https://img.shields.io/badge/Render-Deployed-blueviolet.svg)](#)
[![GitHub Flow](https://img.shields.io/badge/GitHub-Flow-2088FF.svg)](#)

---

## Tabla de Contenidos

- [Descripción del Proyecto](#descripción-del-proyecto)
- [Equipo](#equipo)
- [Arquitectura del Sistema](#arquitectura-del-sistema)
- [Microservicios](#microservicios)
- [API Gateway](#api-gateway)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Estructura del Proyecto (Patrón CSR)](#estructura-del-proyecto-patrón-csr)
- [Requisitos Previos](#requisitos-previos)
- [Ejecución del Proyecto](#ejecución-del-proyecto)
  - [Opción 1: Docker Compose](#opción-1-docker-compose)
  - [Opción 2: Script Local](#opción-2-script-local)
  - [Opción 3: Ejecución Manual por Microservicio](#opción-3-ejecución-manual-por-microservicio)
- [Documentación de API (Swagger)](#documentación-de-api-swagger)
- [Pruebas Unitarias y Cobertura](#pruebas-unitarias-y-cobertura)
- [Comunicación entre Microservicios](#comunicación-entre-microservicios)
- [Base de Datos](#base-de-datos)
- [Despliegue en Render](#despliegue-en-render)
- [Endpoints (Resumen Postman)](#endpoints-resumen-postman)
- [GitHub Flow y Control de Versiones](#github-flow-y-control-de-versiones)
- [Checklist de Evaluación](#checklist-de-evaluación)
- [Licencia](#licencia)

---

## Descripción del Proyecto

**MyMarket** es un sistema de gestión integral para minimarket basado en arquitectura de microservicios. La aplicación permite administrar todas las operaciones críticas de una tienda: catálogo de productos, control de inventario con trazabilidad por lotes, registro de ventas, gestión de proveedores y pedidos, administración de empleados y usuarios, precios dinámicos con descuentos, alertas automáticas por stock y vencimiento, y generación de reportes consolidados.

El proyecto resuelve problemas reales de administración como:
- Control de stock con alertas tempranas (stock bajo, productos vencidos o próximos a vencer)
- Registro y trazabilidad de ventas con descuento automático de inventario
- Gestión de precios variable por temporada con tipos de descuento
- Comunicación entre servicios para mantener la integridad de datos (inventario ↔ ventas ↔ alertas)
- Centralización de accesos mediante API Gateway

---

## Equipo

| Nombre            | GitHub                                         | Rol Principal              |
|-------------------|------------------------------------------------|----------------------------|
| **Benjamin Aguero** | [yountek14](https://github.com/yountek14)     | Arquitectura, DevOps, Swagger, Pruebas |
| **Ignacio Salazar** | [Shir3n](https://github.com/Shir3n)           | Desarrollo de microservicios, Documentación |
| **Luciano Garrido** | [BlackCatVSC](https://github.com/BlackCatVSC) | Desarrollo de microservicios, ClickUp     |

---

## Arquitectura del Sistema

El sistema sigue una **arquitectura de microservicios** con un **API Gateway** como punto único de entrada. Cada microservicio es independiente, con su propia base de datos H2 en memoria, y se comunica con otros servicios mediante llamadas REST usando `WebClient`.

```
                          ┌──────────────────────┐
                          │  API GATEWAY (:8090)  │
                          │  Spring Cloud Gateway │
                          │       + CORS          │
                          └───┬──┬──┬──┬──┬──┬───┘
                              │  │  │  │  │  │
         ┌────────────────────┘  │  │  │  │  └────────────────────┐
         │        ┌──────────────┘  │  │  └──────────┐           │
         │        │      ┌──────────┘  └───┐         │           │
         ▼        ▼      ▼      ▼         ▼         ▼           ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│ ms-productos │ │ ms-inventario│ │  ms-ventas   │ │ ms-alertas   │
│    :8087     │ │    :8083     │ │    :8084     │ │    :8085     │
│              │ │              │ │              │ │              │
│  Catálogo    │ │ Stock/Lotes  │ │  Registro    │ │ Stock bajo   │
│  productos   │ │ Vencimiento   │ │  de ventas   │ │ Vencimiento  │
└──────┬───────┘ └──────┬───────┘ └──────┬───────┘ └──────────────┘
       │                │                │
       │       ◄────────┘                │
       │       │        ◄────────────────┘
       │       │        │
       └───────┼────────┘  (WebClient: validación de producto y descuento de stock)
               │
              (*)
┌──────────────┐ ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│ms-proveedores│ │ ms-pedidos   │ │ ms-empleados │ │ ms-precios   │
│    :8080     │ │    :8081     │ │    :8082     │ │    :8086     │
│              │ │              │ │              │ │              │
│ Proveedores  │ │  Órdenes     │ │  Personal    │ │  Precios +   │
│ Contacto     │ │  Compra      │ │  Turnos      │ │  Descuentos  │
└──────────────┘ └──────────────┘ └──────────────┘ └──────────────┘

┌──────────────┐ ┌──────────────┐
│ ms-usuario   │ │ ms-reportes  │
│    :8088     │ │    :8089     │
│              │ │              │
│  Usuarios    │ │  Reportes    │
│  Roles       │ │  Consolidados│
└──────────────┘ └──────────────┘
```

---

## Microservicios

| # | Microservicio | Puerto | Base de Datos | Descripción |
|---|--------------|--------|---------------|-------------|
| 1 | **ms-productos** | 8087 | H2 | Catálogo de productos: nombre, categoría, unidad de medida, precio base, descripción y fecha de caducidad. |
| 2 | **ms-inventario** | 8083 | H2 | Control de stock con lotes, fechas de vencimiento, merma, stock mínimo/máximo. Se comunica con ms-productos vía WebClient. |
| 3 | **ms-ventas** | 8084 | H2 | Registro de ventas con validación de productos, descuento automático de inventario y cálculo de totales. |
| 4 | **ms-alertas** | 8085 | H2 | Generación automática de alertas por stock bajo, agotado, productos vencidos o próximos a vencer. Consulta ms-inventario y ms-productos. |
| 5 | **ms-proveedores** | 8080 | H2 | Gestión de proveedores: RUT, nombre, dirección, teléfono, email y rubro. |
| 6 | **ms-pedidos** | 8081 | H2 | Órdenes de compra a proveedores con estado, total, observaciones y referencias a proveedor/usuario/empleado. |
| 7 | **ms-empleados** | 8082 | H2 | Administración de personal: nombre, RUT, rol, turno, fecha de contratación y asociación con usuario. |
| 8 | **ms-precios** | 8086 | H2 | Gestión de precios con tipo de descuento (porcentaje/fijo), temporada y rango de fechas de vigencia. |
| 9 | **ms-usuario** | 8088 | H2 | Administración de usuarios del sistema: nombre, email, contraseña y rol. |
| 10 | **ms-reportes** | 8089 | H2 | Generación y consulta de reportes consolidados (ventas diarias, inventario, productos, proveedores). |

### Dependencias entre Microservicios

```
ms-ventas ───────► ms-productos (valida producto)
ms-ventas ───────► ms-inventario (descuenta stock)
ms-inventario ───► ms-productos (obtiene datos del producto)
ms-alertas ──────► ms-inventario (verifica stock/vencimiento)
ms-alertas ──────► ms-productos (obtiene nombre del producto)
```

---

## API Gateway

El **API Gateway** (puerto `8090`) está construido con **Spring Cloud Gateway** y centraliza todas las rutas hacia los microservicios. Actúa como punto único de entrada, aplicando CORS y enrutando según el path de la solicitud.

### Rutas Configuradas

| Path Predicate | Microservicio Destino | Puerto |
|---------------|----------------------|--------|
| `/api/proveedores/**` | ms-proveedores | 8080 |
| `/api/pedidos/**` | ms-pedidos | 8081 |
| `/api/empleados/**` | ms-empleados | 8082 |
| `/api/v1/inventario/**` | ms-inventario | 8083 |
| `/api/v1/ventas/**` | ms-ventas | 8084 |
| `/api/v1/alertas/**` | ms-alertas | 8085 |
| `/api/precios/**` | ms-precios | 8086 |
| `/api/v1/productos/**` | ms-productos | 8087 |
| `/api/usuarios/**` | ms-usuario | 8088 |
| `/api/reportes/**` | ms-reportes | 8089 |

Todas las rutas están parametrizadas con variables de entorno (`API_PRODUCTOS_URL`, etc.), permitiendo cambiar los destinos sin modificar código — ideal para entornos Docker y Render.

---

## Tecnologías Utilizadas

| Categoría | Tecnología |
|-----------|-----------|
| **Lenguaje** | Java 17 |
| **Framework** | Spring Boot 3.4.6 / 4.0.6 |
| **Persistencia** | Spring Data JPA, Hibernate |
| **Base de Datos** | H2 (en memoria) |
| **Documentación** | Springdoc OpenAPI 3.0.2 (Swagger UI + JSON) |
| **API Gateway** | Spring Cloud Gateway 2024.0.1 |
| **Comunicación MS** | WebClient (Spring WebFlux) |
| **Validación** | Jakarta Validation, @Schema (Swagger) |
| **Testing** | JUnit 5, Mockito, JaCoCo 0.8.13 |
| **Build** | Maven (multi-stage Docker builds) |
| **Contenedores** | Docker, Docker Compose |
| **Despliegue Remoto** | Render |
| **Utilidades** | Lombok, SLF4J, ControllerAdvice |
| **Control de Versiones** | Git + GitHub (GitHub Flow) |
| **Gestión de Proyecto** | ClickUp |

---

## Estructura del Proyecto (Patrón CSR)

Cada microservicio sigue el patrón **Controller — Service — Repository / Model**, garantizando separación de responsabilidades y bajo acoplamiento:

```
ms-ejemplo/
├── src/main/java/com/mymarket/ms_ejemplo/
│   ├── MsEjemploApplication.java       # Clase principal con @SpringBootApplication
│   ├── config/
│   │   └── OpenApiConfig.java          # Configuración Swagger/OpenAPI
│   ├── controller/
│   │   └── EjemploController.java      # Endpoints REST (solo orquesta)
│   ├── service/
│   │   └── EjemploService.java         # Lógica de negocio
│   ├── repository/
│   │   └── EjemploRepository.java      # Acceso a datos (JPA)
│   ├── model/
│   │   ├── Ejemplo.java                # Entidad JPA
│   │   ├── EstadoEjemplo.java          # Enumeración
│   │   └── ...                         # Otros modelos/enums
│   ├── dto/
│   │   └── EjemploDTO.java             # DTOs para transferencia
│   └── exception/
│       ├── GlobalExceptionHandler.java # Manejo centralizado de errores
│       └── EntityNotFoundException.java
├── src/test/java/com/mymarket/ms_ejemplo/
│   ├── controller/
│   ├── service/
│   ├── repository/
│   └── exception/
├── src/main/resources/
│   ├── application.yml                 # Configuración del microservicio
│   └── ...
├── Dockerfile
└── pom.xml
```

---

## Requisitos Previos

- **Java 17** o superior ([Eclipse Temurin](https://adoptium.net/) recomendado)
- **Maven 3.9+**
- **Docker** y **Docker Compose** (para ejecución con contenedores)
- **Git** (para clonar el repositorio)

---

## Ejecución del Proyecto

### Clonar el Repositorio

```bash
git clone https://github.com/yountek14/MyMarket.git
cd MyMarket
```

### Opción 1: Docker Compose (Recomendado)

Levanta los 11 servicios (10 microservicios + API Gateway) en contenedores aislados en red interna:

```bash
docker-compose up --build
```

Esto construye las imágenes con el `Dockerfile` de cada servicio y las ejecuta. Los servicios quedarán disponibles en:

- API Gateway: `http://localhost:8090`
- Acceso directo a cada MS: `http://localhost:{puerto}` (ver tabla de microservicios)

Para detener:

```bash
docker-compose down
```

### Opción 2: Script Local (Windows)

Ejecuta `start-all.bat` que abre cada microservicio en una ventana de terminal independiente:

```cmd
start-all.bat
```

### Opción 3: Ejecución Manual por Microservicio

Desde la raíz del proyecto, para cada microservicio:

```bash
cd ms-productos
mvn spring-boot:run
```

Repetir para cada carpeta de microservicio. El orden recomendado es:
1. `ms-productos` (dependencia de varios MS)
2. `ms-inventario` (depende de ms-productos)
3. `ms-ventas`, `ms-alertas`, `ms-proveedores`, `ms-pedidos`, `ms-empleados`, `ms-precios`, `ms-usuario`, `ms-reportes`
4. `ms-gateway` (al final, depende de todos)

---

## Documentación de API (Swagger)

Cada microservicio expone su propia interfaz **Swagger UI** y documentación **OpenAPI JSON**. Una vez el servicio esté corriendo:

| Microservicio | Swagger UI | OpenAPI JSON |
|--------------|------------|--------------|
| ms-productos | http://localhost:8087/swagger | http://localhost:8087/api-docs |
| ms-inventario | http://localhost:8083/swagger | http://localhost:8083/api-docs |
| ms-ventas | http://localhost:8084/swagger | http://localhost:8084/api-docs |
| ms-alertas | http://localhost:8085/swagger | http://localhost:8085/api-docs |
| ms-precios | http://localhost:8086/swagger | http://localhost:8086/api-docs |
| ms-proveedores | http://localhost:8080/swagger | http://localhost:8080/api-docs |
| ms-pedidos | http://localhost:8081/swagger | http://localhost:8081/api-docs |
| ms-empleados | http://localhost:8082/swagger | http://localhost:8082/api-docs |
| ms-usuario | http://localhost:8088/swagger | http://localhost:8088/api-docs |
| ms-reportes | http://localhost:8089/swagger | http://localhost:8089/api-docs |

> También accesibles vía Gateway: `http://localhost:8090/api/v1/productos/swagger` (redirecciona al MS correspondiente).

Todos los modelos, DTOs y enums están documentados con anotaciones **@Schema** que describen sus campos. Cada endpoint incluye descripción, parámetros, códigos de respuesta HTTP (200, 400, 404, 500) y ejemplos JSON.

---

## Pruebas Unitarias y Cobertura

El proyecto cuenta con **247 pruebas unitarias** distribuidas en los 10 microservicios, todas ejecutándose sin errores:

```bash
# Ejecutar pruebas en un microservicio específico
cd ms-productos
mvn test

# Ejecutar todas las pruebas desde la raíz
mvn test
```

| Microservicio | Controller Test | Service Test | Repository Test | ExceptionHandler Test | Total Tests |
|--------------|:--------------:|:------------:|:---------------:|:---------------------:|:-----------:|
| ms-productos | ✓ | ✓ | ✓ | 4 | ~25 |
| ms-inventario | ✓ | ✓ | ✓ | 4 | ~25 |
| ms-ventas | ✓ | ✓ | ✓ | 4 | ~25 |
| ms-alertas | ✓ | ✓ | ✓ | 4 | ~25 |
| ms-precios | ✓ | ✓ | ✓ | 4 | ~25 |
| ms-proveedores | ✓ | ✓ | ✓ | 4 | ~25 |
| ms-pedidos | ✓ | ✓ | ✓ | 4 | ~25 |
| ms-empleados | ✓ | ✓ | ✓ | 4 | ~25 |
| ms-usuario | ✓ | ✓ | ✓ | 4 | ~25 |
| ms-reportes | ✓ | ✓ | ✓ | 4 | ~25 |

**JaCoCo 0.8.13** está configurado en todos los microservicios para generar reportes de cobertura. El resultado verificado supera el **80% mínimo** requerido por la rúbrica (ms-productos alcanzó **98%** de cobertura).

Para generar el reporte de cobertura:

```bash
cd ms-productos
mvn clean test jacoco:report
# Reporte en: target/site/jacoco/index.html
```

Las pruebas siguen el patrón **Given–When–Then** y utilizan **Mockito** para simular repositorios y dependencias externas, asegurando tests aislados y deterministas.

---

## Comunicación entre Microservicios

Los siguientes microservicios se comunican entre sí mediante **WebClient** (Spring WebFlux) realizando llamadas REST:

| Origen | Destino | Propósito |
|--------|---------|-----------|
| ms-ventas | ms-productos | Validar existencia y obtener precio del producto |
| ms-ventas | ms-inventario | Descontar stock tras una venta |
| ms-inventario | ms-productos | Obtener datos del producto asociado al lote |
| ms-alertas | ms-inventario | Verificar stock actual, vencimientos y estado |
| ms-alertas | ms-productos | Obtener nombre y detalles del producto para la alerta |

Los servicios que consumen otros MS exponen un bean `WebClient.Builder` en su clase `@SpringBootApplication`:

```java
@Bean
public WebClient.Builder webClientBuilder() {
    return WebClient.builder();
}
```

Los errores de comunicación remota son manejados por el `GlobalExceptionHandler`, retornando códigos HTTP adecuados (404, 500) con mensajes descriptivos.

---

## Base de Datos

Cada microservicio utiliza su propia base de datos **H2 en memoria**, garantizando independencia total entre servicios. Principales ventajas:

- **Aislamiento**: cada MS gestiona sus propios datos sin compartir esquema
- **Desarrollo rápido**: sin necesidad de instalar bases de datos externas
- **Consola H2**: accesible en `http://localhost:{puerto}/h2-console` para debugging

```
JDBC URL: jdbc:h2:mem:testdb
User: sa
Password: (vacío)
```

---

## Despliegue en Render

3 microservicios están desplegados y operativos en **Render**, accesibles durante la defensa técnica. El despliegue utiliza los mismos `Dockerfile` del repositorio, garantizando consistencia entre entornos locales y remotos.

---

## Endpoints (Resumen Postman)

El archivo `endpoints-postman.txt` contiene la colección completa de endpoints con ejemplos de request/response para pruebas en Postman. A continuación un resumen por microservicio:

| MS | Endpoints Principales |
|----|----------------------|
| **productos** | `GET/POST/PUT/DELETE /api/v1/productos`, búsqueda por categoría, nombre, activo, unidad |
| **inventario** | `GET/POST/PUT/DELETE /api/v1/inventario`, entradas/salidas/merma, búsqueda por lote, vencidos, stock bajo |
| **ventas** | `GET/POST/PUT/DELETE /api/v1/ventas`, pagar/anular venta, búsqueda por fechas |
| **alertas** | `GET/POST/DELETE /api/v1/alertas`, generación automática stock/vencimiento, resolver alerta |
| **precios** | `GET/POST/PUT/DELETE /api/precios`, precio actual por producto, búsqueda por temporada |
| **proveedores** | `GET/POST/PUT/DELETE /api/proveedores`, búsqueda por RUT |
| **pedidos** | `GET/POST/PUT/DELETE /api/pedidos`, búsqueda por estado, proveedor |
| **empleados** | `GET/POST/PUT/DELETE /api/empleados`, búsqueda por rol, turno, usuario |
| **usuario** | `GET/POST/PUT/DELETE /api/usuarios` |
| **reportes** | `GET/POST /api/reportes` |

> Ver `endpoints-postman.txt` para los cuerpos JSON de ejemplo de cada endpoint.

---

## GitHub Flow y Control de Versiones

El proyecto sigue la estrategia **GitHub Flow**:

- **`main`**: rama principal, siempre estable y desplegable
- **Ramas de feature**: `feature/nombre-descriptivo` para cada funcionalidad
- **Pull Requests**: revisión obligatoria por otro integrante antes de mergear
- **Commits**: mensajes técnicos siguiendo Conventional Commits (`feat`, `fix`, `docs`, `test`, `refactor`, `chore`)
- **PRs mergeados**: #9, #10, #11, #12 y subsiguientes

```
main
  ├── feature/docker-gateway-fix       → PR → review → merge
  ├── feature/swagger-jacoco-tests-fix → PR → review → merge
  └── ...
```

Para más detalles, consultar `Guia_GitHub_Flow_Microservicios.md`.

---

## Licencia

Este proyecto es de uso académico para la asignatura **DSY1103 Desarrollo FullStack I**.

---

*MyMarket © 2026 — Benjamin Aguero, Ignacio Salazar, Luciano Garrido*
