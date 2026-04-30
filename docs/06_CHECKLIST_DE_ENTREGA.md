# Checklist de Entrega

## Funcionalidad

- [ ] Existe CRUD de clientes.
- [ ] Existe CRUD de cuentas.
- [ ] Existe CRUD de movimientos.
- [ ] Existe operacion para registrar movimientos contables.
- [ ] Los depósitos suman al saldo disponible.
- [ ] Los retiros restan al saldo disponible.
- [ ] Los movimientos exitosos quedan registrados.
- [ ] Si no hay saldo, se responde `Saldo no disponible`.
- [ ] Existe reporte de estado de cuenta por cliente y rango de fechas.
- [ ] El reporte retorna JSON.

## Arquitectura Semi Senior

- [ ] La solución está separada en 2 microservicios.
- [ ] `cliente-persona-service` maneja Cliente y Persona.
- [ ] `cuenta-movimiento-service` maneja Cuenta y Movimiento.
- [ ] Existe comunicación asincrónica entre microservicios.
- [ ] El servicio de clientes publica eventos de cliente.
- [ ] RabbitMQ transporta los eventos entre microservicios.
- [ ] El servicio de cuentas consume eventos y mantiene snapshot local.

## Buenas prácticas

- [ ] Se usa Spring Data JPA.
- [ ] Se usa patrón Repository.
- [ ] Hay separación Controller / Service / Repository.
- [ ] Se usan DTOs.
- [ ] Hay validaciones de request.
- [ ] Hay excepciones custom.
- [ ] Hay `GlobalExceptionHandler`.
- [ ] Las operaciones de movimiento usan `@Transactional`.
- [ ] No se expone contraseña en responses.
- [ ] Se manejan duplicados de identificación y número de cuenta.

## Tests

- [ ] Hay prueba unitaria de entidad/dominio Cliente.
- [ ] Hay al menos 2 pruebas unitarias de endpoints.
- [ ] Hay prueba para caso `Saldo no disponible`.
- [ ] Hay prueba de integración deseable.

## Docker y despliegue

- [ ] Hay Dockerfile para `cliente-persona-service`.
- [ ] Hay Dockerfile para `cuenta-movimiento-service`.
- [ ] Hay `docker-compose.yml`.
- [ ] Docker Compose levanta PostgreSQL.
- [ ] Docker Compose levanta RabbitMQ.
- [ ] Docker Compose levanta ambos microservicios.

## Base de datos

- [ ] Existe `BaseDatos.sql`.
- [ ] El script crea tablas principales.
- [ ] El script define PKs.
- [ ] El script define unique constraints.
- [ ] El script incluye datos de ejemplo del PDF.

## Postman

- [ ] Existe colección Postman JSON.
- [ ] Incluye endpoints de clientes.
- [ ] Incluye endpoints de cuentas.
- [ ] Incluye endpoint de movimientos.
- [ ] Incluye endpoint de reportes.
- [ ] Incluye caso de error `Saldo no disponible`.

## README

- [ ] Explica arquitectura.
- [ ] Explica stack técnico.
- [ ] Explica cómo correr con Docker.
- [ ] Explica cómo correr tests.
- [ ] Lista endpoints.
- [ ] Explica comunicación asincrónica.
- [ ] Incluye decisiones técnicas defendibles.
