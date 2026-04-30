# Plan de Implementación Paso a Paso para Codex

## Instrucción general para Codex

Implementar este reto como una solución backend Java Spring Boot orientada a perfil Semi Senior, priorizando claridad, buenas prácticas, cumplimiento del contrato y defensa técnica.

No sobreingenierizar. La solución debe correr localmente con Docker Compose.

---

## Paso 1 - Crear estructura del repositorio

Crear un monorepo con esta estructura:

```text
banking-microservices-challenge/
  cliente-persona-service/
  cuenta-movimiento-service/
  docker-compose.yml
  BaseDatos.sql
  postman/
    banking-challenge.postman_collection.json
  docs/
    arquitectura.md
    api-contract.md
  README.md
```

---

## Paso 2 - Crear cliente-persona-service

Crear proyecto Spring Boot con dependencias:

- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Validation
- RabbitMQ / Spring AMQP
- Lombok opcional
- Spring Boot Test

Implementar:

- Entidad Persona.
- Entidad Cliente heredando de Persona.
- ClienteRepository.
- ClienteService.
- ClienteController.
- DTOs de request y response.
- Mapper manual o MapStruct.
- GlobalExceptionHandler.
- Eventos RabbitMQ al crear/actualizar/desactivar cliente.

Endpoints:

- `POST /clientes`
- `GET /clientes`
- `GET /clientes/{id}`
- `PUT /clientes/{id}`
- `PATCH /clientes/{id}`
- `DELETE /clientes/{id}`

---

## Paso 3 - Crear cuenta-movimiento-service

Crear proyecto Spring Boot con dependencias:

- Spring Web
- Spring Data JPA
- PostgreSQL Driver
- Validation
- RabbitMQ / Spring AMQP
- Lombok opcional
- Spring Boot Test

Implementar:

- Entidad Cuenta.
- Entidad Movimiento.
- Entidad ClienteSnapshot.
- CuentaRepository.
- MovimientoRepository.
- ClienteSnapshotRepository.
- CuentaService.
- MovimientoService.
- ReporteService.
- Controllers de Cuenta, Movimiento y Reporte.
- Consumidores RabbitMQ para eventos de cliente.
- GlobalExceptionHandler.

Endpoints:

- `POST /cuentas`
- `GET /cuentas`
- `GET /cuentas/{id}`
- `PUT /cuentas/{id}`
- `DELETE /cuentas/{id}`
- `POST /movimientos`
- `GET /movimientos`
- `GET /movimientos/{id}`
- `PUT /movimientos/{id}`
- `DELETE /movimientos/{id}`
- `GET /reportes?fecha={yyyy-MM-dd},{yyyy-MM-dd}&clienteId={id}`

Nota:

- El PDF solicita CRUD de Movimiento. Por integridad contable, la opcion recomendada es implementar actualizacion y eliminacion como operaciones controladas que informen que un movimiento aplicado no puede modificarse ni eliminarse.
- Para reportes, el contrato principal debe respetar `/reportes?fecha=rango fechas`. Se puede soportar adicionalmente una variante explicita con `clienteId`, `fechaInicio` y `fechaFin` si queda documentada en Postman.

---

## Paso 4 - Implementar regla de movimiento

En `MovimientoService.registrarMovimiento`:

1. Buscar cuenta por `numeroCuenta`.
2. Validar que la cuenta exista y esté activa.
3. Determinar tipo de movimiento:
   - valor positivo = depósito.
   - valor negativo = retiro.
4. Calcular nuevo saldo.
5. Si nuevo saldo es menor a cero, lanzar `SaldoNoDisponibleException("Saldo no disponible")`.
6. Actualizar saldo disponible de cuenta.
7. Guardar movimiento con fecha, tipo, valor y saldo final.
8. Retornar DTO de movimiento creado.

La operación debe estar anotada con `@Transactional`.

---

## Paso 5 - Implementar reporte

En `ReporteService.generarEstadoCuenta`:

1. Validar existencia del cliente.
2. Buscar cuentas asociadas al cliente.
3. Buscar movimientos por cuenta dentro del rango de fechas.
4. Armar JSON con:
   - clienteId
   - cliente
   - fechaInicio
   - fechaFin
   - cuentas
   - movimientos por cuenta

---

## Paso 6 - Excepciones

Crear excepciones custom:

- `ResourceNotFoundException`
- `BusinessException`
- `SaldoNoDisponibleException`
- `DuplicateResourceException`

Crear `GlobalExceptionHandler` con `@RestControllerAdvice`.

Responder errores en formato estándar:

```json
{
  "timestamp": "2026-04-28T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Saldo no disponible",
  "path": "/movimientos"
}
```

Nota:

- El PDF exige manejo de mensajes de excepcion y deja al criterio del candidato la forma de capturar y mostrar errores. El formato JSON estandar es una decision tecnica defendible, no un texto literal del enunciado.

---

## Paso 7 - Tests mínimos

Implementar como mínimo:

### Test unitario de dominio Cliente

Validar creación de Cliente con datos correctos.

Ejemplo de test:

- Debe crear un cliente activo.
- Debe conservar nombre, identificación, teléfono y estado.

### Tests unitarios de endpoints

El PDF pide mínimo dos pruebas unitarias de endpoints.

Implementar con `@WebMvcTest`:

1. `ClienteControllerTest`: crear cliente retorna 201.
2. `MovimientoControllerTest`: retiro sin saldo retorna 400 y mensaje `Saldo no disponible`.

### Test de integración deseable

Con `@SpringBootTest` o Testcontainers:

- Crear cuenta con saldo 100.
- Registrar depósito 600.
- Verificar saldo 700.

---

## Paso 8 - Docker

Crear Dockerfile en cada microservicio.

Crear `docker-compose.yml` con:

- PostgreSQL clientes.
- PostgreSQL cuentas.
- RabbitMQ.
- cliente-persona-service.
- cuenta-movimiento-service.

Exponer puertos:

- cliente-persona-service: 8081.
- cuenta-movimiento-service: 8082.
- RabbitMQ management: 15672.
- PostgreSQL clientes: 5433.
- PostgreSQL cuentas: 5434.

---

## Paso 9 - BaseDatos.sql

Crear script con:

- Tablas de clientes/personas.
- Tablas de cuentas/movimientos/cliente_snapshot.
- Constraints de PK.
- Constraints de unique.
- Datos de ejemplo del PDF.

---

## Paso 10 - README

El README debe explicar:

- Arquitectura.
- Decisiones técnicas.
- Cómo ejecutar con Docker.
- Cómo correr tests.
- Endpoints principales.
- Cómo importar colección Postman.
- Cómo defender la comunicación asincrónica.
