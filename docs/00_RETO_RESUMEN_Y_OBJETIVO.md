# Reto Técnico Backend - Resumen y Objetivo

## Objetivo
Implementar una solución backend para una prueba técnica de arquitectura de microservicios orientada a un perfil **Semi Senior**.

La solución debe cubrir las funcionalidades requeridas para clientes, cuentas, movimientos y reportes, aplicando buenas prácticas de diseño, persistencia, manejo de errores, pruebas y despliegue en Docker.

## Seniority objetivo: Semi Senior
Para este nivel se debe implementar:

- Separación en **2 microservicios**:
  1. `cliente-persona-service`: maneja Cliente y Persona.
  2. `cuenta-movimiento-service`: maneja Cuenta y Movimiento.
- Comunicación **asincrónica** entre microservicios.
- Cumplir funcionalidades: **F1, F2, F3, F4, F5**.
- F6 es deseable.
- F7 aparece como despliegue en contenedores y debe cubrirse porque el PDF también lo pide en indicaciones generales.

## Funcionalidades requeridas

### F1 - CRUDS
Implementar CRUD para:

- Cliente
- Cuenta
- Movimiento

Endpoints base requeridos:

- `/clientes`
- `/cuentas`
- `/movimientos`

### F2 - Registro de movimientos
Al registrar un movimiento:

- El valor puede ser positivo o negativo.
- Debe actualizarse el saldo disponible de la cuenta.
- Debe registrarse la transacción realizada.

### F3 - Saldo no disponible
Si se intenta realizar un movimiento sin saldo suficiente, la API debe responder con el mensaje:

```text
Saldo no disponible
```

Se debe manejar mediante excepciones controladas.

### F4 - Reporte estado de cuenta
Generar un reporte de estado de cuenta especificando:

- Rango de fechas.
- Cliente.

El reporte debe contener:

- Cuentas asociadas con sus saldos.
- Detalle de movimientos de las cuentas.

Endpoint requerido:

```http
GET /reportes?fecha=rango fechas
```

Se recomienda usar un contrato más claro:

```http
GET /reportes?clienteId={clienteId}&fechaInicio={yyyy-MM-dd}&fechaFin={yyyy-MM-dd}
```

Debe retornar JSON.

### F5 - Prueba unitaria
Implementar al menos 1 prueba unitaria para la entidad de dominio Cliente.

### F6 - Prueba de integración deseable
Implementar al menos 1 prueba de integración si el tiempo lo permite.

### F7 - Docker
La solución debe desplegarse en contenedores Docker.

## Entregables esperados

- Código fuente en repositorio Git público.
- `BaseDatos.sql` con script de base de datos, entidades y esquema de datos.
- Colección Postman JSON para validar endpoints.
- Dockerfile por microservicio.
- `docker-compose.yml` para levantar servicios, base de datos y mensajería.
- README con instrucciones de ejecución.
