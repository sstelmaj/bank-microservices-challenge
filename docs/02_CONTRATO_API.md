# Contrato de API

## Convención general

Para máxima fidelidad al enunciado del reto, los endpoints se expondrán exactamente como se solicitan en el PDF:

```http
/clientes
/cuentas
/movimientos
/reportes
```

No se utilizará prefijo `/api/v1` en los controllers principales.

Base URL local de ejemplo:

```http
http://localhost:8080
```

> Nota técnica: aunque en proyectos reales es común versionar APIs usando `/api/v1`, en esta prueba se prioriza respetar el contrato solicitado para facilitar la validación con Postman/Karate.

---

# Cliente API

El endpoint `/clientes` pertenece al microservicio `cliente-persona-service`.

## Crear cliente

```http
POST /clientes
```

Request:

```json
{
  "nombre": "Jose Lema",
  "genero": "MASCULINO",
  "edad": 35,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "contrasena": "1234",
  "estado": true
}
```

Response 201:

```json
{
  "clienteId": 1,
  "nombre": "Jose Lema",
  "genero": "MASCULINO",
  "edad": 35,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "estado": true
}
```

Notas:

- `identificacion` debe ser única.
- `contrasena` no debe retornarse en las respuestas.
- Al crear un cliente, el servicio debe publicar un evento asincrónico para que el microservicio de cuentas pueda registrar o actualizar su referencia local del cliente.

---

## Listar clientes

```http
GET /clientes
```

Response 200:

```json
[
  {
    "clienteId": 1,
    "nombre": "Jose Lema",
    "genero": "MASCULINO",
    "edad": 35,
    "identificacion": "1234567890",
    "direccion": "Otavalo sn y principal",
    "telefono": "098254785",
    "estado": true
  },
  {
    "clienteId": 2,
    "nombre": "Marianela Montalvo",
    "genero": "FEMENINO",
    "edad": 30,
    "identificacion": "0987654321",
    "direccion": "Amazonas y NNUU",
    "telefono": "097548965",
    "estado": true
  }
]
```

---

## Obtener cliente por ID

```http
GET /clientes/{clienteId}
```

Response 200:

```json
{
  "clienteId": 1,
  "nombre": "Jose Lema",
  "genero": "MASCULINO",
  "edad": 35,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "estado": true
}
```

Response 404:

```json
{
  "timestamp": "2026-04-28T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cliente no encontrado",
  "path": "/clientes/1"
}
```

---

## Actualizar cliente completo

```http
PUT /clientes/{clienteId}
```

Request:

```json
{
  "nombre": "Jose Lema Actualizado",
  "genero": "MASCULINO",
  "edad": 36,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "contrasena": "1234",
  "estado": true
}
```

Response 200:

```json
{
  "clienteId": 1,
  "nombre": "Jose Lema Actualizado",
  "genero": "MASCULINO",
  "edad": 36,
  "identificacion": "1234567890",
  "direccion": "Otavalo sn y principal",
  "telefono": "098254785",
  "estado": true
}
```

Notas:

- Después de actualizar un cliente, debe publicarse un evento asincrónico `ClienteActualizadoEvent`.

---

## Actualizar parcialmente cliente

```http
PATCH /clientes/{clienteId}
```

Request ejemplo:

```json
{
  "direccion": "Nueva dirección 123",
  "telefono": "099999999",
  "estado": true
}
```

Response 200:

```json
{
  "clienteId": 1,
  "nombre": "Jose Lema",
  "genero": "MASCULINO",
  "edad": 35,
  "identificacion": "1234567890",
  "direccion": "Nueva dirección 123",
  "telefono": "099999999",
  "estado": true
}
```

---

## Eliminar cliente

```http
DELETE /clientes/{clienteId}
```

Response 204:

```http
No Content
```

Recomendación técnica:

- Implementar borrado lógico cambiando `estado=false`.
- Publicar un evento asincrónico `ClienteEliminadoEvent` o `ClienteDesactivadoEvent`.

---

# Cuenta API

El endpoint `/cuentas` pertenece al microservicio `cuenta-movimiento-service`.

## Crear cuenta

```http
POST /cuentas
```

Request:

```json
{
  "numeroCuenta": "478758",
  "tipoCuenta": "AHORRO",
  "saldoInicial": 2000,
  "estado": true,
  "clienteId": 1
}
```

Response 201:

```json
{
  "id": 1,
  "numeroCuenta": "478758",
  "tipoCuenta": "AHORRO",
  "saldoInicial": 2000,
  "saldoDisponible": 2000,
  "estado": true,
  "clienteId": 1
}
```

Notas:

- `numeroCuenta` debe ser único.
- `saldoDisponible` debe iniciar con el mismo valor que `saldoInicial`.
- Antes de crear la cuenta, se debe validar que exista el cliente en la referencia local del microservicio de cuentas.

---

## Listar cuentas

```http
GET /cuentas
```

Response 200:

```json
[
  {
    "id": 1,
    "numeroCuenta": "478758",
    "tipoCuenta": "AHORRO",
    "saldoInicial": 2000,
    "saldoDisponible": 1425,
    "estado": true,
    "clienteId": 1
  },
  {
    "id": 2,
    "numeroCuenta": "225487",
    "tipoCuenta": "CORRIENTE",
    "saldoInicial": 100,
    "saldoDisponible": 700,
    "estado": true,
    "clienteId": 2
  }
]
```

---

## Obtener cuenta por ID

```http
GET /cuentas/{id}
```

Response 200:

```json
{
  "id": 1,
  "numeroCuenta": "478758",
  "tipoCuenta": "AHORRO",
  "saldoInicial": 2000,
  "saldoDisponible": 1425,
  "estado": true,
  "clienteId": 1
}
```

---

## Actualizar cuenta

```http
PUT /cuentas/{id}
```

Request:

```json
{
  "numeroCuenta": "478758",
  "tipoCuenta": "AHORRO",
  "saldoInicial": 2000,
  "estado": true,
  "clienteId": 1
}
```

Response 200:

```json
{
  "id": 1,
  "numeroCuenta": "478758",
  "tipoCuenta": "AHORRO",
  "saldoInicial": 2000,
  "saldoDisponible": 1425,
  "estado": true,
  "clienteId": 1
}
```

Nota:

- No se recomienda modificar manualmente `saldoDisponible` desde este endpoint. El saldo disponible debe cambiar mediante movimientos.

---

## Eliminar cuenta

```http
DELETE /cuentas/{id}
```

Response 204:

```http
No Content
```

Recomendación técnica:

- Implementar borrado lógico cambiando `estado=false`.

---

# Movimiento API

El endpoint `/movimientos` pertenece al microservicio `cuenta-movimiento-service`.

## Crear movimiento

```http
POST /movimientos
```

Request para depósito:

```json
{
  "numeroCuenta": "225487",
  "valor": 600
}
```

Request para retiro:

```json
{
  "numeroCuenta": "478758",
  "valor": -575
}
```

Reglas de negocio:

- Valor positivo = depósito.
- Valor negativo = retiro.
- Al realizar un movimiento, se debe actualizar el saldo disponible de la cuenta.
- Se debe llevar registro histórico de los movimientos realizados.
- Si el saldo resultante es menor a 0, se debe responder con el mensaje exacto: `Saldo no disponible`.
- Solo deben registrarse movimientos exitosos.

Response 201 para depósito:

```json
{
  "id": 1,
  "fecha": "2022-02-10T10:30:00",
  "tipoMovimiento": "DEPOSITO",
  "valor": 600,
  "saldo": 700,
  "numeroCuenta": "225487"
}
```

Response 201 para retiro:

```json
{
  "id": 2,
  "fecha": "2022-02-08T09:15:00",
  "tipoMovimiento": "RETIRO",
  "valor": -540,
  "saldo": 0,
  "numeroCuenta": "496825"
}
```

Response 400 por saldo insuficiente:

```json
{
  "timestamp": "2026-04-28T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Saldo no disponible",
  "path": "/movimientos"
}
```

---

## Listar movimientos

```http
GET /movimientos
```

Response 200:

```json
[
  {
    "id": 1,
    "fecha": "2022-02-10T10:30:00",
    "tipoMovimiento": "DEPOSITO",
    "valor": 600,
    "saldo": 700,
    "numeroCuenta": "225487"
  },
  {
    "id": 2,
    "fecha": "2022-02-08T09:15:00",
    "tipoMovimiento": "RETIRO",
    "valor": -540,
    "saldo": 0,
    "numeroCuenta": "496825"
  }
]
```

---

## Obtener movimiento por ID

```http
GET /movimientos/{id}
```

Response 200:

```json
{
  "id": 1,
  "fecha": "2022-02-10T10:30:00",
  "tipoMovimiento": "DEPOSITO",
  "valor": 600,
  "saldo": 700,
  "numeroCuenta": "225487"
}
```

---

## Actualizar movimiento

```http
PUT /movimientos/{id}
```

Nota importante:

Por integridad contable, no es recomendable editar movimientos ya aplicados, porque modificar un movimiento histórico obliga a recalcular saldos posteriores. Sin embargo, como el PDF solicita CRUD para Movimiento, se puede resolver de una de estas dos formas:

1. Implementar el endpoint y devolver un error funcional indicando que los movimientos aplicados no pueden editarse.
2. Permitir edición controlada solo de metadatos no contables.

Opción recomendada para el reto:

```json
{
  "timestamp": "2026-04-28T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "No se puede editar un movimiento ya aplicado",
  "path": "/movimientos/1"
}
```

---

## Eliminar movimiento

```http
DELETE /movimientos/{id}
```

Nota importante:

Por integridad contable, no se recomienda eliminación física de movimientos. Para cumplir CRUD, se puede implementar borrado lógico o responder error funcional.

Opción recomendada para el reto:

```json
{
  "timestamp": "2026-04-28T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "No se puede eliminar un movimiento ya aplicado",
  "path": "/movimientos/1"
}
```

---

# Reportes API

El endpoint `/reportes` pertenece al microservicio `cuenta-movimiento-service`.

## Estado de cuenta

Endpoint alineado al PDF:

```http
GET /reportes?fecha=2022-02-01,2022-02-28&clienteId=2
```

Parámetros:

- `fecha`: rango de fechas en formato `yyyy-MM-dd,yyyy-MM-dd`.
- `clienteId`: identificador del cliente.

Alternativa más explícita, aceptable si se documenta en Postman:

```http
GET /reportes?clienteId=2&fechaInicio=2022-02-01&fechaFin=2022-02-28
```

Response 200:

```json
{
  "clienteId": 2,
  "cliente": "Marianela Montalvo",
  "fechaInicio": "2022-02-01",
  "fechaFin": "2022-02-28",
  "cuentas": [
    {
      "numeroCuenta": "225487",
      "tipo": "CORRIENTE",
      "saldoInicial": 100,
      "estado": true,
      "saldoDisponible": 700,
      "movimientos": [
        {
          "fecha": "2022-02-10",
          "movimiento": 600,
          "saldoDisponible": 700
        }
      ]
    },
    {
      "numeroCuenta": "496825",
      "tipo": "AHORRO",
      "saldoInicial": 540,
      "estado": true,
      "saldoDisponible": 0,
      "movimientos": [
        {
          "fecha": "2022-02-08",
          "movimiento": -540,
          "saldoDisponible": 0
        }
      ]
    }
  ]
}
```

Formato alternativo plano compatible con el ejemplo del PDF:

```json
[
  {
    "fecha": "2022-02-10",
    "cliente": "Marianela Montalvo",
    "numeroCuenta": "225487",
    "tipo": "Corriente",
    "saldoInicial": 100,
    "estado": true,
    "movimiento": 600,
    "saldoDisponible": 700
  },
  {
    "fecha": "2022-02-08",
    "cliente": "Marianela Montalvo",
    "numeroCuenta": "496825",
    "tipo": "Ahorros",
    "saldoInicial": 540,
    "estado": true,
    "movimiento": -540,
    "saldoDisponible": 0
  }
]
```

Recomendación:

- Usar el formato agrupado por cliente y cuentas como respuesta principal.
- Incluir el formato plano solo si se quiere replicar exactamente el ejemplo visual del PDF.

---

# Errores

Formato estándar recomendado:

```json
{
  "timestamp": "2026-04-28T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Saldo no disponible",
  "path": "/movimientos"
}
```

Errores mínimos:

- `400 Bad Request`: validación de request, saldo no disponible o regla de negocio incumplida.
- `404 Not Found`: entidad no encontrada.
- `409 Conflict`: número de cuenta, identificación u otro campo único duplicado.
- `500 Internal Server Error`: error no controlado.

Ejemplo 404:

```json
{
  "timestamp": "2026-04-28T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Cuenta no encontrada",
  "path": "/cuentas/99"
}
```

Ejemplo 409:

```json
{
  "timestamp": "2026-04-28T10:30:00",
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe una cuenta con el número 478758",
  "path": "/cuentas"
}
```

---

# Validaciones recomendadas

## Cliente

- `nombre`: obligatorio.
- `identificacion`: obligatoria y única.
- `telefono`: obligatorio.
- `contrasena`: obligatoria.
- `estado`: obligatorio.

## Cuenta

- `numeroCuenta`: obligatorio y único.
- `tipoCuenta`: obligatorio. Valores sugeridos: `AHORRO`, `CORRIENTE`.
- `saldoInicial`: obligatorio, mayor o igual a 0.
- `estado`: obligatorio.
- `clienteId`: obligatorio.

## Movimiento

- `numeroCuenta`: obligatorio.
- `valor`: obligatorio y distinto de 0.
- El saldo resultante no puede quedar negativo.

---

# Endpoints mínimos esperados para Postman/Karate

```http
POST   /clientes
GET    /clientes
GET    /clientes/{clienteId}
PUT    /clientes/{clienteId}
PATCH  /clientes/{clienteId}
DELETE /clientes/{clienteId}

POST   /cuentas
GET    /cuentas
GET    /cuentas/{id}
PUT    /cuentas/{id}
DELETE /cuentas/{id}

POST   /movimientos
GET    /movimientos
GET    /movimientos/{id}
PUT    /movimientos/{id}
DELETE /movimientos/{id}

GET    /reportes?fecha=2022-02-01,2022-02-28&clienteId=2
```
