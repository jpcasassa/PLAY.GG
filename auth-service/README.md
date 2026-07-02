# auth-service

## Para que sirve

Gestiona registro y login basico para PLAY.GG.

## Utilidad en el proyecto

Permite registrar un usuario e iniciar sesion sin sesiones propias. El servicio consulta `user-service` mediante Feign: para registrar llama a `POST /users`, y para login consulta los datos internos por email.

## Datos que maneja

Este servicio no tiene entidad JPA ni base de datos propia. Trabaja con DTOs:

- `RegisterRequestDTO`: datos de registro.
- `LoginRequestDTO`: email y password.
- `AuthResponseDTO`: datos basicos del usuario autenticado.

## Puerto

`8082`

## Endpoints

- `POST /auth/register`: registra usuario usando `user-service`.
- `POST /auth/login`: valida email y password consultando `user-service`.

## Datos que pide register

```json
{
  "nickname": "playerOne",
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan@mail.com",
  "password": "12345678",
  "country": "Chile"
}
```

## Datos que pide login

```json
{
  "email": "juan@mail.com",
  "password": "12345678"
}
```

## Datos que devuelve

```json
{
  "userId": 1,
  "nickname": "playerOne",
  "email": "juan@mail.com",
  "role": "PLAYER",
  "message": "Login correcto"
}
```

## Comunicacion con otros servicios

Usa Feign para consultar `user-service`. No accede directamente a la base de datos de usuarios.

## Spring Security

El proyecto mantiene una configuracion simple de Spring Security con usuario en memoria (`admin/admin123`) para usar HTTP Basic si se agregan endpoints protegidos. Los endpoints `/auth/register` y `/auth/login` quedan publicos para las pruebas.

## Resumen

Este servicio separa autenticacion de gestion de usuarios. `auth-service` valida credenciales, pero los datos del usuario viven en `user-service`.

## Guia para leer el codigo

Este microservicio sigue el flujo:

```text
Controller -> Service -> Feign Client -> user-service
```

Archivos principales:

- `src/main/java/com/playgg/auth/AuthServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/auth/controller/AuthController.java`: expone `/auth/register` y `/auth/login`.
- `src/main/java/com/playgg/auth/service/AuthService.java`: contiene la validacion de credenciales.
- `src/main/java/com/playgg/auth/client/UserClient.java`: cliente Feign hacia `user-service`.
- `src/main/java/com/playgg/auth/dto/`: contiene objetos de entrada y salida.
- `src/main/resources/application.yml`: configura puerto, nombre del servicio y Eureka.

Que editar segun el cambio:

- Nuevo endpoint: editar el Controller.
- Nueva regla de negocio: editar el Service.
- Cambiar puerto: editar `server.port` en `application.yml`.
- Cambiar nombre registrado en Eureka: editar `spring.application.name`.
- Cambiar comunicacion con usuarios: editar `client/UserClient.java`.

## Pruebas con Thunder Client

Usar preferentemente Gateway:

```text
http://localhost:8080/auth
```

Tambien se puede probar directo si el servicio esta levantado:

```text
http://localhost:8082/auth
```

Registro:

```text
POST http://localhost:8080/auth/register
```

```json
{
  "nickname": "playerOne",
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan@mail.com",
  "password": "12345678",
  "country": "Chile"
}
```

Login:

```text
POST http://localhost:8080/auth/login
```

```json
{
  "email": "juan@mail.com",
  "password": "12345678"
}
```

Antes de probar por Gateway deben estar levantados `discovery-service`, `gateway-service`, `user-service` y `auth-service`.

## Pruebas con Swagger

Este servicio tambien se puede probar desde Swagger UI.

1. Levantar `discovery-service`.
2. Levantar `user-service`.
3. Levantar `auth-service`.
4. Abrir:

```text
http://localhost:8082/swagger-ui.html
```

Tambien se puede ver la especificacion OpenAPI en:

```text
http://localhost:8082/v3/api-docs
```

En Swagger abrir `/auth/register` o `/auth/login`, presionar `Try it out`, completar el JSON y ejecutar con `Execute`. Estos endpoints y las rutas de Swagger estan permitidos en Spring Security.

## Pruebas Unitarias

Este microservicio tiene pruebas unitarias con JUnit 5 y Mockito en `src/test/java/com/playgg/auth/service/AuthServiceTest.java`.

La clase probada es `AuthService`. Se mockea solo `UserClient`, por lo que no se realizan llamadas HTTP reales a `user-service` ni se levanta Spring Boot.

Los tests cubren:

- Registrar usuario correctamente.
- Iniciar sesion correctamente.
- Manejar error cuando `user-service` no devuelve usuario al registrar.
- Manejar error por password incorrecta.
- Manejar error cuando el usuario esta inactivo.

Mockito se usa para crear mocks, que son objetos simulados que reemplazan dependencias reales durante el test. En microservicios esto permite probar la logica de negocio de forma aislada, sin depender de otros servicios.

Para ejecutar:

```bash
mvn test
```
