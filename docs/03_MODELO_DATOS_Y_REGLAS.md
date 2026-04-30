# Modelo de Datos y Reglas de Negocio

Este documento parte de las entidades y funcionalidades solicitadas por el PDF. Las reglas de validacion adicionales se incluyen como decisiones de diseno defendibles para un perfil Semi Senior, siempre que no contradigan el enunciado.

## Entidades

## Persona

Campos:

- id: Long, PK.
- nombre: String, obligatorio.
- genero: Enum o String.
- edad: Integer.
- identificacion: String, obligatorio, único.
- direccion: String.
- telefono: String.

## Cliente

Cliente hereda de Persona.

Campos adicionales:

- clienteId: Long, PK o identificador único del cliente.
- contrasena: String, obligatorio.
- estado: Boolean.

Reglas:

- `identificacion` debe ser unica como decision de integridad de negocio.
- `contrasena` no debe exponerse en respuestas o reportes.

Implementación recomendada en JPA:

Opción simple:

- Usar herencia `@Inheritance(strategy = InheritanceType.JOINED)`.
- `Persona` como entidad base.
- `Cliente` extiende `Persona`.

Opción práctica para prueba técnica:

- Usar `@MappedSuperclass` para Persona.
- `Cliente` extiende Persona y se persiste en una sola tabla `clientes`.

Para defender semi senior, la opción `JOINED` demuestra mejor manejo de herencia JPA. La opción `MappedSuperclass` reduce complejidad.

## Cuenta

Campos:

- id: Long, PK.
- numeroCuenta: String, único, obligatorio.
- tipoCuenta: Enum `AHORRO`, `CORRIENTE`.
- saldoInicial: BigDecimal, obligatorio.
- saldoDisponible: BigDecimal, obligatorio.
- estado: Boolean.
- clienteId: Long, obligatorio.

Reglas:

- `saldoInicial` no debe ser negativo.
- Al crear la cuenta, `saldoDisponible = saldoInicial`.
- `numeroCuenta` debe ser único.
- La cuenta debe pertenecer a un cliente existente o al menos a un cliente presente en el snapshot local.
- Las reglas de saldo inicial no negativo y numero de cuenta unico complementan el PDF para proteger consistencia financiera.

## Movimiento

Campos:

- id: Long, PK.
- fecha: LocalDateTime.
- tipoMovimiento: Enum `DEPOSITO`, `RETIRO`.
- valor: BigDecimal.
- saldo: BigDecimal.
- cuenta: Cuenta.

Reglas:

- `valor` no puede ser cero.
- Si `valor > 0`, tipo = `DEPOSITO`.
- Si `valor < 0`, tipo = `RETIRO`.
- Nuevo saldo = saldo actual de la cuenta + valor.
- Si nuevo saldo < 0, lanzar excepción con mensaje `Saldo no disponible`.
- Registrar el movimiento con el saldo final luego de aplicar la operación.
- Actualizar saldo disponible de la cuenta dentro de la misma transacción.
- Por integridad contable, un movimiento ya aplicado no debe modificarse ni eliminarse fisicamente; si se implementa CRUD completo, la actualizacion o eliminacion debe resolverse como operacion controlada o rechazo funcional documentado.

## ClienteSnapshot

Entidad local opcional en `cuenta-movimiento-service` para soportar comunicación asincrónica.

Campos:

- clienteId: Long.
- nombre: String.
- identificacion: String.
- estado: Boolean.

Uso:

- Se crea o actualiza al consumir eventos desde `cliente-persona-service`.
- Permite que `cuenta-movimiento-service` valide clientes sin llamada síncrona HTTP.

## Reglas críticas

### Saldo insuficiente

Cuando el usuario intenta retirar más dinero del disponible:

```text
saldoDisponible + valor < 0
```

Se debe lanzar:

```text
Saldo no disponible
```

### Reporte de estado de cuenta

Debe filtrar por:

- Cliente.
- Fecha inicio.
- Fecha fin.

Debe retornar:

- Datos del cliente.
- Cuentas asociadas.
- Saldos actuales.
- Movimientos por cuenta dentro del rango solicitado.

## Datos de ejemplo para seed

Clientes:

| Nombre | Dirección | Teléfono | Contraseña | Estado |
|---|---|---|---|---|
| Jose Lema | Otavalo sn y principal | 098254785 | 1234 | true |
| Marianela Montalvo | Amazonas y NNUU | 097548965 | 5678 | true |
| Juan Osorio | 13 junio y Equinoccial | 098874587 | 1245 | true |

Cuentas:

| Número Cuenta | Tipo | Saldo Inicial | Estado | Cliente |
|---|---|---:|---|---|
| 478758 | AHORRO | 2000 | true | Jose Lema |
| 225487 | CORRIENTE | 100 | true | Marianela Montalvo |
| 495878 | AHORRO | 0 | true | Juan Osorio |
| 496825 | AHORRO | 540 | true | Marianela Montalvo |
| 585545 | CORRIENTE | 1000 | true | Jose Lema |

Movimientos:

| Número Cuenta | Movimiento |
|---|---:|
| 478758 | -575 |
| 225487 | 600 |
| 495878 | 150 |
| 496825 | -540 |
