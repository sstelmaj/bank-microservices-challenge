# bank-microservices-challenge

Reto backend para un sistema bancario simple implementado con Java y Spring Boot, siguiendo una arquitectura de dos microservicios para perfil Semi Senior.

## Microservicios

- `cliente-persona-service`: gestiona Persona y Cliente.
- `cuenta-movimiento-service`: gestiona Cuenta, Movimiento y reportes.

## Stack base

- Java 21
- Spring Boot 3.5.x
- Maven multi-module
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- RabbitMQ
- Spring Boot Actuator
- Docker Compose

## Estructura

```text
bank-microservices-challenge/
  cliente-persona-service/
  cuenta-movimiento-service/
  docker-compose.yml
  pom.xml
```

## Ejecucion local con Docker

### Prerrequisitos

- Docker Desktop o Docker Engine con Docker Compose
- Java 21 y Maven solo si quieres ejecutar pruebas fuera de los contenedores

### Levantar toda la solucion

Desde la raiz del repositorio:

```bash
docker compose up --build
```

Esto levanta:

- `postgres-clientes`
- `postgres-cuentas`
- `rabbitmq`
- `cliente-persona-service`
- `cuenta-movimiento-service`

### Servicios expuestos

- `cliente-persona-service`: `http://localhost:8081`
- `cuenta-movimiento-service`: `http://localhost:8082`
- RabbitMQ Management: `http://localhost:15672`

Credenciales por defecto de RabbitMQ:

- usuario: `bank_user`
- contrasena: `bank_pass`

### Verificacion rapida

Health checks:

```bash
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
```

Si todo arranco bien, ambos endpoints deben responder con estado `UP`.

### Variables de entorno principales

`cliente-persona-service`

- `CLIENTE_PERSONA_PORT`
- `CLIENTES_DB_URL`
- `CLIENTES_DB_USERNAME`
- `CLIENTES_DB_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
- `RABBITMQ_HOST`
- `RABBITMQ_PORT`
- `RABBITMQ_USERNAME`
- `RABBITMQ_PASSWORD`

`cuenta-movimiento-service`

- `CUENTA_MOVIMIENTO_PORT`
- `CUENTAS_DB_URL`
- `CUENTAS_DB_USERNAME`
- `CUENTAS_DB_PASSWORD`
- `SPRING_JPA_HIBERNATE_DDL_AUTO`
- `RABBITMQ_HOST`
- `RABBITMQ_PORT`
- `RABBITMQ_USERNAME`
- `RABBITMQ_PASSWORD`

### Detener la solucion

```bash
docker compose down
```

Para eliminar tambien los volumenes:

```bash
docker compose down -v
```

## Comandos utiles

Ejecutar pruebas de ambos microservicios:

```bash
mvn -pl cliente-persona-service,cuenta-movimiento-service test
```

Validar la configuracion final de Docker Compose:

```bash
docker compose config
```

Ver logs de los servicios:

```bash
docker compose logs -f
```

## Flujo de trabajo

El desarrollo debe seguir `AGENTS.md`:

- `main` como rama principal.
- `develop` como rama de integracion.
- ramas `feat/<descripcion-corta>` para cada funcionalidad.
- commits pequenos, trazables y con TDD para funcionalidades de negocio.

## Estado actual

La solucion cuenta con dos microservicios Spring Boot, persistencia separada en PostgreSQL para cada contexto y RabbitMQ como broker de mensajeria para la comunicacion asincronica entre servicios.
