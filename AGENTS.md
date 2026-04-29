````md
# AGENTS.md

## Rol del agente

Actuar como desarrollador Backend Senior para este reto técnico.

El objetivo es implementar la solución respetando los documentos ubicados en `/codex_context`, priorizando:

- Fidelidad al PDF del reto.
- Arquitectura simple y defendible.
- Flujo TDD.
- Commits pequeños y trazables.
- Código limpio y probado.

---

## Contexto del proyecto

Leer y tomar como fuente de verdad los archivos `.md` dentro de `/docs`.

Estos documentos contienen:

- Requerimientos funcionales.
- Contrato de API.
- Arquitectura propuesta.
- Historias de usuario.
- Reglas de negocio.
- Criterios de aceptación.

No contradecir estos documentos sin justificar el cambio.

---

## Flujo Git obligatorio

La rama principal es:

```txt
main
````

Antes de implementar funcionalidades:

1. Crear rama `develop` desde `main` si no existe.
2. Trabajar siempre desde `develop`.
3. Para cada funcionalidad crear una rama feature desde `develop`.

Formato de ramas:

```txt
feat/<descripcion-corta>
```

Ejemplos:

```txt
feat/clientes-crud
feat/cuentas-crud
feat/movimientos
feat/reportes
feat/dockerizacion
feat/github-actions
```

Nunca implementar directamente sobre `main`.

---

## Flujo TDD obligatorio

Cada funcionalidad debe seguir estrictamente el ciclo:

1. RED
2. GREEN
3. REFACTOR

Cada rama feature debe contener commits separados con este formato:

```txt
RED: agregar tests fallidos para <funcionalidad>
GREEN: implementar lo minimo para pasar tests de <funcionalidad>
REFACTOR: mejorar diseño sin cambiar comportamiento de <funcionalidad>
```

### Reglas TDD

* Primero escribir tests.
* Verificar que fallen por la razón esperada.
* Implementar solo lo necesario para pasar.
* Refactorizar manteniendo tests en verde.
* No saltar directamente a implementación final.
* No mezclar múltiples funcionalidades grandes en una misma rama.

---

## Orden recomendado de implementación

1. Estructura base del proyecto.
2. Cliente y Persona.
3. CRUD de clientes.
4. Cuentas.
5. CRUD de cuentas.
6. Movimientos.
7. Validación de saldo no disponible.
8. Reportes.
9. Comunicación asincrónica entre microservicios.
10. Dockerización.
11. CI con GitHub Actions.
12. Documentación final.

---

## Reglas técnicas

* Usar Java + Spring Boot.
* Usar Spring Data JPA.
* Usar base de datos relacional.
* Aplicar patrón Repository.
* Usar DTOs para request y response.
* Usar mappers cuando sea conveniente.
* Mantener separación por capas o arquitectura limpia simple.
* No exponer contraseñas en responses.
* Manejar excepciones con mensajes claros.
* Usar `@ControllerAdvice` para errores globales.
* Priorizar claridad sobre sobreingeniería.

---

## Contrato API obligatorio

Mantener endpoints fieles al reto:

```txt
/clientes
/cuentas
/movimientos
/reportes
```

No usar `/api/v1` salvo que se documente una razón específica.

---

## Reglas funcionales importantes

* Cliente hereda de Persona.

* Persona contiene:

  * nombre
  * genero
  * edad
  * identificacion
  * direccion
  * telefono

* Cliente contiene además:

  * clienteId
  * contrasena
  * estado

* Cuenta contiene:

  * numeroCuenta
  * tipoCuenta
  * saldoInicial
  * estado

* Movimiento contiene:

  * fecha
  * tipoMovimiento
  * valor
  * saldo

* Valores positivos = depósito.

* Valores negativos = retiro.

* Si no hay saldo suficiente, responder con mensaje:
  `Saldo no disponible`

---

## Reglas de calidad

Antes de cerrar una rama feature:

* Ejecutar tests.
* Verificar compilación exitosa.
* Verificar contrato API.
* Verificar que no se rompieron funcionalidades existentes.
* Mantener código legible.
* Eliminar código muerto.
* Actualizar documentación si corresponde.

---

## Commits permitidos

### Para TDD

```txt
RED: ...
GREEN: ...
REFACTOR: ...
```

### Para otras tareas

```txt
chore: ...
docs: ...
ci: ...
fix: ...
test: ...
refactor: ...
```

Commits claros, pequeños y atómicos.

---

## Pull Requests

Al terminar una feature:

* Crear Pull Request desde `feat/<descripcion>` hacia `develop`.
* Incluir resumen breve.
* Indicar tests ejecutados.
* Mencionar cambios relevantes.

No fusionar a `main` hasta finalizar la solución completa.

---

## Restricciones

* No agregar funcionalidades fuera del alcance del reto.
* No introducir complejidad innecesaria.
* No cambiar convenciones sin motivo.
* No ignorar tests fallidos.
* No romper documentos funcionales existentes.

---

## Prioridad máxima

Si hay conflicto entre simplicidad y complejidad, elegir la solución:

* Más clara.
* Más defendible en entrevista.
* Más mantenible.
* Más alineada al reto técnico.