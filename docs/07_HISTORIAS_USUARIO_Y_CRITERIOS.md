# Historias de Usuario y Criterios de Aceptacion

## HU-01: Gestionar clientes

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | usuario administrativo del sistema bancario |
| **Quiero** | registrar, consultar, actualizar y desactivar clientes |
| **Para** | mantener actualizada la informacion de las personas que pueden tener productos bancarios |

### Criterios de aceptacion

**Criterio 1 - Registrar un cliente nuevo**  
- **Dado** un cliente nuevo con nombre, identificacion, telefono, contrasena y estado,  
- **Cuando** el usuario administrativo registra el cliente,  
- **Entonces** el cliente queda disponible para consulta y uso en la gestion bancaria.

**Criterio 2 - Registrar un cliente con identificacion duplicada**  
- **Dado** un cliente con identificacion ya registrada,  
- **Cuando** el usuario administrativo intenta registrar otro cliente con la misma identificacion,  
- **Entonces** el sistema informa que la identificacion ya existe y no duplica el cliente.

**Criterio 3 - Actualizar un cliente existente**  
- **Dado** un cliente existente,  
- **Cuando** el usuario administrativo actualiza sus datos personales o de contacto,  
- **Entonces** el sistema muestra la informacion actualizada del cliente.

**Criterio 4 - Desactivar un cliente**  
- **Dado** un cliente existente,  
- **Cuando** el usuario administrativo lo desactiva,  
- **Entonces** el cliente queda marcado como inactivo y no debe tratarse como cliente activo para nuevas operaciones.

**Criterio 5 - Ocultar la contrasena**  
- **Dado** cualquier consulta de clientes,  
- **Cuando** el sistema presenta la informacion del cliente,  
- **Entonces** la contrasena no se muestra como parte de los datos visibles.

## HU-02: Consultar clientes

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | usuario administrativo del sistema bancario |
| **Quiero** | consultar clientes registrados |
| **Para** | revisar su informacion antes de gestionar cuentas u operaciones asociadas |

### Criterios de aceptacion

**Criterio 1 - Listar clientes**  
- **Dado** que existen clientes registrados,  
- **Cuando** el usuario administrativo consulta el listado de clientes,  
- **Entonces** el sistema muestra los clientes con sus datos principales y su estado.

**Criterio 2 - Consultar detalle de un cliente**  
- **Dado** un cliente registrado,  
- **Cuando** el usuario administrativo consulta su detalle,  
- **Entonces** el sistema muestra la informacion completa disponible del cliente, excepto datos sensibles.

**Criterio 3 - Cliente no encontrado**  
- **Dado** que el cliente solicitado no existe,  
- **Cuando** el usuario administrativo intenta consultarlo,  
- **Entonces** el sistema informa que el cliente no fue encontrado.

## HU-03: Gestionar cuentas bancarias

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | usuario administrativo del sistema bancario |
| **Quiero** | crear, consultar, actualizar y desactivar cuentas bancarias |
| **Para** | administrar los productos financieros asociados a cada cliente |

### Criterios de aceptacion

**Criterio 1 - Crear una cuenta para un cliente activo**  
- **Dado** un cliente existente y activo,  
- **Cuando** el usuario administrativo crea una cuenta con numero, tipo, saldo inicial y estado,  
- **Entonces** la cuenta queda asociada al cliente y disponible para operaciones.

**Criterio 2 - Inicializar saldo disponible**  
- **Dado** una cuenta nueva con saldo inicial,  
- **Cuando** la cuenta es creada,  
- **Entonces** el saldo disponible de la cuenta inicia con el mismo valor del saldo inicial.

**Criterio 3 - Rechazar saldo inicial negativo**  
- **Dado** una cuenta con saldo inicial negativo,  
- **Cuando** el usuario administrativo intenta crearla,  
- **Entonces** el sistema rechaza la operacion e informa que el saldo inicial no es valido.

**Criterio 4 - Evitar numero de cuenta duplicado**  
- **Dado** un numero de cuenta ya registrado,  
- **Cuando** el usuario administrativo intenta crear otra cuenta con el mismo numero,  
- **Entonces** el sistema informa que el numero de cuenta ya existe y no crea una cuenta duplicada.

**Criterio 5 - Validar cliente no disponible**  
- **Dado** un cliente inexistente o no disponible para operar,  
- **Cuando** el usuario administrativo intenta crearle una cuenta,  
- **Entonces** el sistema rechaza la operacion e informa que el cliente no es valido.

**Criterio 6 - Actualizar cuenta existente**  
- **Dado** una cuenta existente,  
- **Cuando** el usuario administrativo actualiza sus datos permitidos,  
- **Entonces** el sistema muestra la cuenta con la informacion actualizada sin alterar manualmente el saldo disponible.

**Criterio 7 - Desactivar cuenta**  
- **Dado** una cuenta existente,  
- **Cuando** el usuario administrativo la desactiva,  
- **Entonces** la cuenta queda marcada como inactiva para nuevas operaciones.

## HU-04: Consultar cuentas bancarias

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | usuario administrativo del sistema bancario |
| **Quiero** | consultar cuentas registradas |
| **Para** | conocer su tipo, estado, cliente asociado y saldo disponible |

### Criterios de aceptacion

**Criterio 1 - Listar cuentas**  
- **Dado** que existen cuentas registradas,  
- **Cuando** el usuario administrativo consulta el listado de cuentas,  
- **Entonces** el sistema muestra las cuentas con numero, tipo, saldo inicial, saldo disponible, estado y cliente asociado.

**Criterio 2 - Consultar detalle de una cuenta**  
- **Dado** una cuenta registrada,  
- **Cuando** el usuario administrativo consulta su detalle,  
- **Entonces** el sistema muestra la informacion vigente de la cuenta.

**Criterio 3 - Cuenta no encontrada**  
- **Dado** que la cuenta solicitada no existe,  
- **Cuando** el usuario administrativo intenta consultarla,  
- **Entonces** el sistema informa que la cuenta no fue encontrada.

## HU-05: Registrar depositos

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | usuario operativo del sistema bancario |
| **Quiero** | registrar depositos en una cuenta |
| **Para** | aumentar el saldo disponible y conservar el historial de la transaccion |

### Criterios de aceptacion

**Criterio 1 - Clasificar un valor positivo como deposito**  
- **Dado** una cuenta existente y activa,  
- **Cuando** el usuario operativo registra un movimiento con valor positivo,  
- **Entonces** el sistema lo clasifica como deposito.

**Criterio 2 - Aumentar el saldo disponible**  
- **Dado** una cuenta con saldo disponible,  
- **Cuando** se registra un deposito,  
- **Entonces** el saldo disponible aumenta por el valor depositado.

**Criterio 3 - Registrar el deposito en el historial**  
- **Dado** un deposito registrado correctamente,  
- **Cuando** el usuario consulta el historial de movimientos,  
- **Entonces** el movimiento aparece con su fecha, valor, tipo de operacion y saldo resultante.

**Criterio 4 - Rechazar valor igual a cero**  
- **Dado** un movimiento con valor igual a cero,  
- **Cuando** el usuario operativo intenta registrarlo,  
- **Entonces** el sistema rechaza la operacion porque el valor del movimiento debe modificar el saldo.

## HU-06: Registrar retiros

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | usuario operativo del sistema bancario |
| **Quiero** | registrar retiros en una cuenta |
| **Para** | disminuir el saldo disponible y conservar el historial de la transaccion |

### Criterios de aceptacion

**Criterio 1 - Clasificar un valor negativo como retiro**  
- **Dado** una cuenta existente y activa,  
- **Cuando** el usuario operativo registra un movimiento con valor negativo,  
- **Entonces** el sistema lo clasifica como retiro.

**Criterio 2 - Disminuir el saldo disponible**  
- **Dado** una cuenta con saldo disponible suficiente,  
- **Cuando** se registra un retiro,  
- **Entonces** el saldo disponible disminuye por el valor retirado.

**Criterio 3 - Registrar el retiro en el historial**  
- **Dado** un retiro registrado correctamente,  
- **Cuando** el usuario consulta el historial de movimientos,  
- **Entonces** el movimiento aparece con su fecha, valor, tipo de operacion y saldo resultante.

**Criterio 4 - Aceptar saldo exacto en cero**  
- **Dado** un retiro cuyo valor deja la cuenta con saldo exacto de cero,  
- **Cuando** el usuario operativo registra el movimiento,  
- **Entonces** la operacion se acepta y la cuenta queda con saldo disponible igual a cero.

## HU-07: Controlar saldo insuficiente

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | usuario operativo del sistema bancario |
| **Quiero** | que el sistema impida retiros superiores al saldo disponible |
| **Para** | evitar que una cuenta quede con saldo negativo |

### Criterios de aceptacion

**Criterio 1 - Rechazar retiro sin saldo suficiente**  
- **Dado** una cuenta con saldo disponible menor al valor que se desea retirar,  
- **Cuando** el usuario operativo intenta registrar el retiro,  
- **Entonces** el sistema informa "Saldo no disponible".

**Criterio 2 - Conservar el saldo de la cuenta**  
- **Dado** un retiro rechazado por saldo insuficiente,  
- **Cuando** el usuario consulta la cuenta,  
- **Entonces** el saldo disponible permanece sin cambios.

**Criterio 3 - No registrar el movimiento rechazado**  
- **Dado** un retiro rechazado por saldo insuficiente,  
- **Cuando** el usuario consulta el historial de movimientos,  
- **Entonces** el movimiento rechazado no aparece como transaccion registrada.

## HU-08: Consultar movimientos

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | usuario administrativo del sistema bancario |
| **Quiero** | consultar los movimientos registrados |
| **Para** | revisar el historial de transacciones aplicadas sobre las cuentas |

### Criterios de aceptacion

**Criterio 1 - Listar movimientos**  
- **Dado** que existen movimientos exitosos registrados,  
- **Cuando** el usuario administrativo consulta el listado de movimientos,  
- **Entonces** el sistema muestra cada movimiento con fecha, tipo de operacion, valor, cuenta y saldo resultante.

**Criterio 2 - Consultar detalle de un movimiento**  
- **Dado** un movimiento registrado,  
- **Cuando** el usuario administrativo consulta su detalle,  
- **Entonces** el sistema muestra la informacion de la transaccion aplicada.

**Criterio 3 - Movimiento no encontrado**  
- **Dado** que el movimiento solicitado no existe,  
- **Cuando** el usuario administrativo intenta consultarlo,  
- **Entonces** el sistema informa que el movimiento no fue encontrado.

**Criterio 4 - Proteger la integridad historica**  
- **Dado** un movimiento ya aplicado al saldo de una cuenta,  
- **Cuando** el usuario administrativo intenta modificarlo o eliminarlo,  
- **Entonces** el sistema impide alterar la integridad historica de la transaccion.

## HU-09: Generar reporte de estado de cuenta

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | cliente bancario o usuario administrativo |
| **Quiero** | generar un estado de cuenta por cliente y rango de fechas |
| **Para** | conocer las cuentas, saldos y movimientos realizados en un periodo |

### Criterios de aceptacion

**Criterio 1 - Mostrar las cuentas del cliente**  
- **Dado** un cliente con cuentas asociadas,  
- **Cuando** el usuario solicita el estado de cuenta para un rango de fechas,  
- **Entonces** el sistema muestra la informacion del cliente, sus cuentas y los saldos vigentes.

**Criterio 2 - Incluir movimientos dentro del rango**  
- **Dado** que existen movimientos dentro del rango de fechas seleccionado,  
- **Cuando** se genera el estado de cuenta,  
- **Entonces** el sistema incluye el detalle de esos movimientos en la cuenta correspondiente.

**Criterio 3 - Excluir movimientos fuera del rango**  
- **Dado** que existen movimientos fuera del rango de fechas seleccionado,  
- **Cuando** se genera el estado de cuenta,  
- **Entonces** el sistema no incluye esos movimientos en el resultado.

**Criterio 4 - Mostrar cuentas sin movimientos en el periodo**  
- **Dado** un cliente sin movimientos en el rango seleccionado,  
- **Cuando** se genera el estado de cuenta,  
- **Entonces** el sistema muestra sus cuentas con sus saldos y una lista de movimientos vacia para el periodo.

**Criterio 5 - Validar rango de fechas**  
- **Dado** un rango de fechas invalido,  
- **Cuando** el usuario intenta generar el estado de cuenta,  
- **Entonces** el sistema informa que el rango de fechas no es valido.

**Criterio 6 - Cliente no encontrado**  
- **Dado** un cliente inexistente,  
- **Cuando** el usuario intenta generar el estado de cuenta,  
- **Entonces** el sistema informa que el cliente no fue encontrado.

## HU-10: Mantener disponibilidad del cliente para cuentas

| Aspecto | Descripcion |
|---------|-------------|
| **Como** | usuario administrativo del sistema bancario |
| **Quiero** | que los cambios del cliente se reflejen para la gestion de cuentas |
| **Para** | evitar crear o administrar productos con informacion desactualizada |

### Criterios de aceptacion

**Criterio 1 - Habilitar al cliente para cuentas**  
- **Dado** un cliente registrado correctamente,  
- **Cuando** el cliente queda disponible en el sistema bancario,  
- **Entonces** puede ser usado para asociar nuevas cuentas.

**Criterio 2 - Usar datos vigentes del cliente**  
- **Dado** un cliente actualizado,  
- **Cuando** el usuario consulta informacion relacionada con sus cuentas,  
- **Entonces** el sistema utiliza los datos vigentes del cliente disponibles para la operacion.

**Criterio 3 - Impedir nuevas cuentas para un cliente desactivado**  
- **Dado** un cliente desactivado,  
- **Cuando** el usuario intenta crearle una nueva cuenta,  
- **Entonces** el sistema rechaza la operacion porque el cliente no esta activo.

**Criterio 4 - Validar disponibilidad del cliente**  
- **Dado** que la informacion del cliente aun no esta disponible para cuentas,  
- **Cuando** el usuario intenta asociarle una cuenta,  
- **Entonces** el sistema informa que el cliente no puede ser validado en ese momento.
