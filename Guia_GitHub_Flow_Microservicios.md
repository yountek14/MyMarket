# Guía de GitHub Flow para el Proyecto de Microservicios

**Asignatura:** DSY1103 — Desarrollo FullStack
**Contexto:** Proyecto semestral de Arquitectura de Microservicios (Evaluación Parcial 3)

Esta guía te enseña qué es **GitHub Flow**, por qué conviene usarlo en un proyecto con 10+ microservicios trabajado en equipo, y cómo implementarlo **paso a paso en el repositorio que ya tienen creado**.

---

## 1. ¿Qué es GitHub Flow?

GitHub Flow es una **estrategia de ramas (branching) ligera** pensada para equipos que integran cambios de forma frecuente. Su idea central es muy simple:

> La rama `main` **siempre** está estable y desplegable. Todo cambio nuevo se hace en una rama corta y descriptiva, se revisa con un Pull Request y recién entonces se integra a `main`.

No es un sistema complicado: son 6 pasos que se repiten una y otra vez.

```
main (siempre estable)
  │
  ├──● feature/auth-service-login      → PR → review → merge → main
  │
  ├──● fix/gateway-timeout             → PR → review → merge → main
  │
  └──● docs/readme-endpoints           → PR → review → merge → main
```

### El ciclo de GitHub Flow

1. **Partir desde `main` actualizado.**
2. **Crear una rama** con nombre descriptivo (`feature/...`, `fix/...`).
3. **Trabajar y commitear** en esa rama (commits pequeños y claros).
4. **Subir la rama** (`push`) a GitHub.
5. **Abrir un Pull Request (PR)** para que el equipo lo revise.
6. **Hacer merge a `main`** una vez aprobado, y borrar la rama.

---

## 2. ¿Para qué sirve y por qué les conviene en este proyecto?

En un proyecto de microservicios con varios integrantes, varias personas tocan el código **al mismo tiempo**. GitHub Flow sirve para:

- **Mantener `main` siempre funcionando.** Si `main` siempre compila y despliega, en cualquier momento pueden mostrar el sistema corriendo (clave para la defensa técnica y para el despliegue en Docker/Render).
- **Aislar el trabajo.** Cada microservicio o funcionalidad vive en su propia rama, así un cambio a medio terminar no rompe el trabajo de los demás.
- **Revisar antes de integrar.** El Pull Request obliga a que otro integrante mire el código antes de mezclarlo. Detecta errores temprano.
- **Tener un historial limpio y trazable.** Cada PR cuenta una historia: qué se hizo, quién y por qué. Esto se evalúa directamente en la rúbrica (commits técnicos, progresivos y distribuidos).

---

## 3. GitHub Flow vs. ramas con nombre y apellido (¿por qué NO `juan-perez`?)

Una práctica común (y poco profesional) es que cada integrante cree una rama con su nombre: `juan-perez`, `maria-soto`, etc. Comparemos:

| Aspecto | Rama por **persona** (`juan-perez`) | Rama por **trabajo** (GitHub Flow: `feature/order-service`) |
|---|---|---|
| ¿Qué cambia la rama? | No se sabe; hay que abrirla para adivinar | Está en el nombre: una funcionalidad concreta |
| Tamaño de los cambios | Gigante: meses de trabajo mezclado | Pequeño y enfocado: fácil de revisar |
| Conflictos al integrar | Frecuentes y enormes | Pocos y manejables |
| Code review | Imposible (PR de 3.000 líneas) | Real (PR de un cambio acotado) |
| Si algo se rompe | Difícil saber qué commit lo causó | Se identifica el PR culpable al instante |
| ¿`main` queda estable? | No garantizado | Sí, siempre |
| Historial | "juan subió cosas" | "Se agregó login al auth-service" |
| En la industria | No se usa | Es el estándar real |

**Conclusión:** las ramas no se organizan por *quién* trabaja, sino por *qué* se está construyendo. Una misma persona puede abrir muchas ramas distintas (`feature/...`, `fix/...`) durante el proyecto, y un mismo microservicio puede recibir aportes de varios integrantes en distintas ramas.

---

## 4. Categorías de ramas: cuándo usar cada prefijo

El nombre de la rama se forma con un **prefijo de categoría** + una **descripción corta en minúsculas separada por guiones**:

```
categoria/descripcion-corta-y-clara
```

| Prefijo | Cuándo usarlo | Ejemplos en su proyecto |
|---|---|---|
| `feature/` | Una funcionalidad o microservicio **nuevo** | `feature/payment-service`, `feature/swagger-order-service`, `feature/gateway-routes` |
| `fix/` | Corregir un **bug** detectado durante el desarrollo | `fix/auth-token-expiration`, `fix/yaml-port-conflict` |
| `hotfix/` | Corrección **urgente** sobre algo ya en `main` que está roto | `hotfix/gateway-down` |
| `refactor/` | Mejorar/reordenar código **sin cambiar su comportamiento** | `refactor/csr-product-service`, `refactor/remove-duplicated-dto` |
| `test/` | Agregar o mejorar **pruebas unitarias** | `test/order-service-jacoco`, `test/inventory-coverage-80` |
| `docs/` | Cambios de **documentación** (README, comentarios, Swagger) | `docs/readme-endpoints`, `docs/api-gateway-diagram` |
| `chore/` | Tareas de mantenimiento que no son código de negocio (configs, dependencias, Docker) | `chore/dockerfile-order-service`, `chore/update-dependencies` |

> **Regla de oro:** el nombre debe decir *qué* hace la rama sin necesidad de abrirla. `feature/agregar-carrito` es bueno; `feature/cambios` o `juan` son malos.

---

## 5. Implementación paso a paso en su repositorio YA creado

Asumimos que el equipo ya tiene el repositorio en GitHub (con los microservicios) y lo tienen clonado localmente. Estos son los comandos.

### Paso 0 — Verificar que `main` esté actualizado

Antes de crear cualquier rama, sincroniza tu copia local con lo último de GitHub:

```bash
git checkout main
git pull origin main
```

> Hazlo **siempre** antes de empezar algo nuevo. Así partes desde la última versión y evitas conflictos innecesarios.

### Paso 1 — Crear tu rama de trabajo

Por ejemplo, vas a implementar Swagger en el `order-service`:

```bash
git checkout -b feature/swagger-order-service
```

`-b` crea la rama y te cambia a ella en un solo comando. Confirma en qué rama estás con:

```bash
git branch
```

(El asterisco `*` indica tu rama actual.)

### Paso 2 — Trabajar y hacer commits pequeños

Edita el código y guarda tu avance en commits **pequeños y descriptivos**. Un buen mensaje explica *qué* y *por qué*:

```bash
git add .
git commit -m "feat(order-service): habilita springdoc-openapi y UI de Swagger"
```

Puedes hacer varios commits a medida que avanzas:

```bash
git add .
git commit -m "docs(order-service): documenta endpoints GET y POST en Swagger"
```

> **Estilo de mensaje recomendado (Conventional Commits):** `tipo(alcance): descripción`.
> Tipos útiles: `feat`, `fix`, `docs`, `test`, `refactor`, `chore`.
> Evita mensajes no técnicos como "cambios", "avance" o "subo cosas" — la rúbrica penaliza eso.

### Paso 3 — Subir tu rama a GitHub

La primera vez que subes una rama nueva, usa `-u` para enlazarla:

```bash
git push -u origin feature/swagger-order-service
```

Las siguientes veces basta con:

```bash
git push
```

### Paso 4 — Abrir el Pull Request (PR)

1. Entra al repositorio en GitHub.
2. Aparecerá un aviso **"Compare & pull request"** para tu rama. Haz clic.
3. Escribe un **título claro** y una **descripción** de qué hiciste (qué microservicio, qué endpoints, cómo probarlo).
4. Asigna a un compañero como **reviewer** y crea el PR.

> El PR es el corazón de GitHub Flow: es donde el equipo revisa, comenta y aprueba antes de mezclar a `main`.

### Paso 5 — Revisar y hacer merge

- El reviewer mira el código, deja comentarios y aprueba.
- Si piden cambios, los haces en la **misma rama**: nuevos commits + `git push`. El PR se actualiza solo.
- Una vez aprobado, presiona **"Merge pull request"** en GitHub.

### Paso 6 — Limpiar la rama y volver a empezar

Después del merge, borra la rama (GitHub ofrece un botón "Delete branch") y actualiza tu local:

```bash
git checkout main
git pull origin main
git branch -d feature/swagger-order-service
```

¡Y vuelves al Paso 1 para la siguiente tarea! Ese es todo el ciclo.

---

## 6. Comandos rápidos de referencia (cheat sheet)

```bash
# Actualizar main antes de empezar
git checkout main
git pull origin main

# Crear y moverse a una rama nueva
git checkout -b feature/nombre-descriptivo

# Ver en qué rama estoy
git branch

# Guardar avances
git add .
git commit -m "feat(servicio): descripción clara"

# Subir la rama (primera vez)
git push -u origin feature/nombre-descriptivo
# Subir cambios siguientes
git push

# Cambiarme a otra rama existente
git checkout nombre-de-la-rama

# Traer cambios nuevos de main a mi rama (para evitar conflictos)
git checkout feature/mi-rama
git merge main

# Después del merge del PR: limpiar
git checkout main
git pull origin main
git branch -d feature/nombre-descriptivo
```

---

## 7. Buenas prácticas para el equipo

- **`main` es sagrado:** nunca trabajen directamente en `main`. Todo entra por PR.
- **Ramas cortas:** que vivan días, no semanas. Cambios pequeños = revisiones rápidas.
- **Una rama = una cosa:** no mezclen "agregué Swagger y de paso arreglé el Gateway". Sepárenlo en dos ramas.
- **Pull antes de push:** actualicen `main` y mezclen a su rama seguido, para que los conflictos sean chicos.
- **Reviewer obligatorio:** que un PR lo apruebe alguien distinto de quien lo escribió. Así reparten el conocimiento del proyecto (clave para la defensa **individual**).
- **Distribuyan el trabajo:** que todos los integrantes abran ramas y PRs. La rúbrica evalúa commits **distribuidos equitativamente**.

---

## 8. Cómo esto te beneficia directamente en la evaluación

Trabajar con GitHub Flow no es solo "lo correcto": te suma puntos concretos en la rúbrica de la EP3:

- **Repositorio ordenado con commits técnicos, progresivos y distribuidos** → el flujo lo produce de forma natural.
- **Historial claro y profesional** → cada PR documenta una decisión técnica que podrás explicar en la defensa.
- **`main` siempre desplegable** → facilita el despliegue en **Docker** y **Render** sin sorpresas el día de la evaluación.
- **Conocimiento repartido** → como la defensa es individual, revisar los PRs de tus compañeros te prepara para explicar todo el proyecto, no solo tu parte.

---

> **Resumen en una frase:** trabaja en ramas pequeñas con nombres que digan *qué* haces (`feature/`, `fix/`, `docs/`...), intégralas a `main` mediante Pull Requests revisados, y mantén `main` siempre listo para desplegar.
