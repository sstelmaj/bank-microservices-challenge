# Arquitectura y Decisiones Técnicas

## Stack recomendado

Usar Java con Spring Boot, porque el PDF permite Java Spring Boot y exige manejo de entidades con JPA.

Stack propuesto:

- Java 21 o Java 17.
- Spring Boot 3.x.
- Spring Web.
- Spring Data JPA.
- PostgreSQL.
- RabbitMQ para comunicación asincrónica.
- Docker y Docker Compose.
- JUnit 5.
- Mockito.
- Testcontainers deseable para integración.
- Flyway opcional, pero igualmente debe existir `BaseDatos.sql`.
- Lombok opcional.
- MapStruct opcional.

## Microservicios

### 1. cliente-persona-service
Responsabilidades:

- Gestión de clientes.
- Persistencia de datos de Persona y Cliente.
- Exponer CRUD `/clientes`.
- Publicar evento asincrónico cuando se cree, actualice o elimine un cliente.

Entidades:

- Persona
- Cliente

### 2. cuenta-movimiento-service
Responsabilidades:

- Gestión de cuentas.
- Gestión de movimientos.
- Validación de saldo.
- Actualización de saldo disponible.
- Registro histórico de transacciones.
- Generación de reporte de estado de cuenta.
- Consumir eventos de cliente para mantener una copia mínima local del cliente o validar existencia de cliente.

Entidades:

- Cuenta
- Movimiento
- ClienteSnapshot o ClienteReferencia

## Comunicación asincrónica

Para cumplir el requisito Semi Senior de comunicación asincrónica, usar RabbitMQ.

Eventos recomendados:

### ClienteCreadoEvent
Publicado por `cliente-persona-service` y consumido por `cuenta-movimiento-service`.

Campos:

- clienteId
- nombre
- identificacion
- estado

### ClienteActualizadoEvent
Actualiza el snapshot local en `cuenta-movimiento-service`.

### ClienteEliminadoEvent o ClienteDesactivadoEvent
Marca el cliente como inactivo en `cuenta-movimiento-service`.

## Estrategia de datos

Cada microservicio debe tener su propia base de datos o, como mínimo, su propio esquema lógico.

Propuesta para Docker:

- `clientes_db`
- `cuentas_db`

En una prueba técnica se puede usar un solo PostgreSQL con dos bases de datos para simplificar.

## Buenas prácticas esperadas

Aplicar:

- Separación por capas.
- Patrón Repository.
- DTOs para entrada y salida.
- Mappers para convertir entre entidades y DTOs.
- Servicios de dominio/aplicación para reglas de negocio.
- Excepciones custom.
- ControllerAdvice global.
- Validaciones con Bean Validation.
- Transacciones en operaciones críticas.
- Tests unitarios de dominio y endpoints.
- Dockerización completa.

## Estructura sugerida por microservicio

```text
src/main/java/com/reto/{servicio}/
  application/
    service/
    dto/
    mapper/
  domain/
    model/
    exception/
    repository/
  infrastructure/
    persistence/
    messaging/
    web/
      controller/
      advice/
  config/
```

También es aceptable una estructura más simple por capas:

```text
controller/
service/
repository/
entity/
dto/
exception/
config/
messaging/
```

Para semi senior, priorizar claridad y defensa técnica por encima de sobreingeniería.
