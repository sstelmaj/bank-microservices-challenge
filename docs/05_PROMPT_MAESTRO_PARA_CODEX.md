# Prompt Maestro para Codex

Actúa como un desarrollador Backend Java Spring Boot Semi Senior.

Necesito implementar una prueba técnica backend de arquitectura de microservicios para un sistema bancario simple con clientes, cuentas, movimientos y reportes.

## Contexto obligatorio

Debes leer y respetar los documentos `.md` de esta carpeta como fuente de verdad:

- `00_RETO_RESUMEN_Y_OBJETIVO.md`
- `01_ARQUITECTURA_Y_DECISIONES.md`
- `02_CONTRATO_API.md`
- `03_MODELO_DATOS_Y_REGLAS.md`
- `04_IMPLEMENTACION_PASO_A_PASO_CODEX.md`
- `06_CHECKLIST_DE_ENTREGA.md`
- `07_HISTORIAS_USUARIO_Y_CRITERIOS.md`

## Requisitos principales

Implementar una solución Java Spring Boot con 2 microservicios:

1. `cliente-persona-service`
   - Maneja Persona y Cliente.
   - Expone CRUD de `/clientes`.
   - Publica eventos asincrónicos cuando se crea, actualiza o elimina/desactiva un cliente.

2. `cuenta-movimiento-service`
   - Maneja Cuenta y Movimiento.
   - Expone CRUD de `/cuentas` y `/movimientos`.
   - Genera reporte `/reportes` por cliente y rango de fechas.
   - Consume eventos asincrónicos de cliente y mantiene un `ClienteSnapshot` local.

Nota:

- El CRUD de movimientos debe contemplar creacion, consulta, actualizacion y eliminacion. Por integridad contable, actualizacion y eliminacion pueden resolverse como operaciones controladas que rechazan modificar transacciones ya aplicadas.

## Stack técnico

Usar:

- Java 17 o 21.
- Spring Boot 3.x.
- Spring Web.
- Spring Data JPA.
- PostgreSQL.
- RabbitMQ con Spring AMQP.
- Bean Validation.
- JUnit 5.
- Mockito.
- Docker y Docker Compose.

## Reglas de negocio críticas

- Un movimiento puede tener valor positivo o negativo.
- Valor positivo = depósito.
- Valor negativo = retiro.
- Al registrar un movimiento, actualizar el saldo disponible de la cuenta.
- Si el saldo resultante es menor a cero, responder con mensaje exacto: `Saldo no disponible`.
- Todo movimiento exitoso debe quedar registrado.
- El reporte debe retornar JSON con cuentas del cliente, saldos y detalle de movimientos por rango de fechas.

## Calidad esperada

Aplicar:

- Repository Pattern.
- DTOs para request/response.
- Services para reglas de negocio.
- Mappers.
- Excepciones custom.
- `@RestControllerAdvice` para errores.
- `@Transactional` en operaciones críticas.
- Validaciones con `jakarta.validation`.
- Tests unitarios mínimos.
- Dockerización completa.

## Tests requeridos

Implementar al menos:

1. Prueba unitaria de dominio Cliente.
2. Prueba unitaria endpoint Cliente Controller.
3. Prueba unitaria endpoint Movimiento Controller validando `Saldo no disponible`.
4. Prueba de integración deseable para depósito y actualización de saldo.

## Entregables que debes generar

- Código completo de ambos microservicios.
- `docker-compose.yml`.
- Dockerfile por microservicio.
- `BaseDatos.sql`.
- Colección Postman JSON.
- README principal.

## Forma de trabajo

Implementa de manera incremental:

1. Primero crea estructura del monorepo.
2. Luego implementa `cliente-persona-service`.
3. Luego implementa `cuenta-movimiento-service`.
4. Luego agrega RabbitMQ.
5. Luego agrega tests.
6. Luego agrega Docker.
7. Finalmente genera README, BaseDatos.sql y colección Postman.

Antes de escribir código, revisa si hay contradicciones entre documentos. Si las hay, prioriza:

1. Requisitos explícitos del PDF resumidos en `00_RETO_RESUMEN_Y_OBJETIVO.md`.
2. Contrato de API en `02_CONTRATO_API.md`.
3. Simplicidad defendible para perfil Semi Senior.
