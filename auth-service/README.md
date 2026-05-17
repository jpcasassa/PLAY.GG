# auth-service

## Para que sirve

Gestiona autenticacion, registro, sesiones y generacion de tokens JWT.

## Utilidad en el proyecto

Permite iniciar sesion y obtener un token para identificar al usuario. En esta version universitaria el flujo es simple y entendible: se valida el usuario consultando `user-service` mediante Feign y luego se genera un JWT.

## Datos que maneja

Entidad `AuthSession`:

- `sessionId`
- `userId`
- `token`
- `refreshToken`
- `createdAt`
- `expiresAt`
- `revoked`

## Base de datos

`playgg_auth_db`

## Puerto

`8082`

## Endpoints

- `POST /auth/register`: registra usuario usando `user-service` y crea sesion.
- `POST /auth/login`: valida credenciales y genera token.
- `POST /auth/refresh`: renueva token usando refresh token.
- `POST /auth/logout`: revoca una sesion.

## Spring Security y autenticacion basica

Este servicio incluye `spring-boot-starter-security` y una configuracion simple en `SecurityConfig`.

Para mantener el flujo entendible:

- `/auth/register` queda publico para crear usuarios.
- `/auth/login` queda publico para iniciar sesion.
- Los demas endpoints de `auth-service` requieren autenticacion HTTP Basic.

Usuario de prueba para Basic Auth:

```text
username: admin
password: admin123
```

Ejemplo usando curl:

```bash
curl -u admin:admin123 http://localhost:8082/auth/refresh
```

En una aplicacion real, los usuarios de seguridad no deberian estar en memoria ni escritos en codigo. Para este proyecto universitario se usa asi porque es mas facil de probar y defender tecnicamente.

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
  "token": "jwt...",
  "refreshToken": "jwt...",
  "role": "PLAYER"
}
```

## Comunicacion con otros servicios

Usa Feign para consultar `user-service`. No accede directamente a la base de datos de usuarios.

## Para la defensa

JWT sirve para representar una sesion de forma compacta. El token contiene datos basicos como usuario y rol. Este servicio separa autenticacion de gestion de usuarios, manteniendo responsabilidades claras.

Tambien se puede explicar que Spring Security se usa para demostrar una autenticacion basica con HTTP Basic. El cliente envia usuario y clave en la cabecera `Authorization`, y Spring valida esos datos antes de permitir el acceso al endpoint protegido.

## Guia para leer el codigo

Este microservicio sigue arquitectura CSR: Controller -> Service -> Repository.

Archivos principales:

- `src/main/java/com/playgg/auth/AuthServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/auth/controller/AuthController.java`: recibe las peticiones HTTP con `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.
- `src/main/java/com/playgg/auth/service/AuthService.java`: contiene la logica de negocio, validaciones de existencia, conversion a DTO y logs.
- `src/main/java/com/playgg/auth/repository/AuthSessionRepository.java`: conecta con la base de datos usando `JpaRepository`.
- `src/main/java/com/playgg/auth/model/`: contiene las entidades JPA. En este servicio la entidad principal es `AuthSession`.
- `src/main/java/com/playgg/auth/dto/`: contiene los objetos de entrada y salida. Sirven para no exponer directamente la entidad.
- `src/main/java/com/playgg/auth/exception/GlobalExceptionHandler.java`: centraliza errores 400, 404 y 500.
- `src/main/resources/application.yml`: configura puerto, nombre del servicio, Eureka y MySQL.

Como se conecta cada capa:

1. El cliente llama un endpoint del Controller.
2. El Controller recibe parametros como `@PathVariable` o JSON con `@RequestBody`.
3. El Controller llama al Service.
4. El Service aplica reglas y usa el Repository.
5. El Repository ejecuta operaciones JPA en MySQL.
6. El Service devuelve un DTO de respuesta.
7. El Controller responde con `ResponseEntity`.

Que editar segun el cambio:

- Nuevo endpoint: editar el Controller.
- Nueva regla de negocio: editar el Service.
- Nuevo campo en la tabla: editar Model, DTOs y mapper del Service.
- Nueva consulta a base de datos: agregar metodo en Repository.
- Cambiar conexion MySQL: editar `src/main/resources/application.yml`.
- Cambiar puerto: editar `server.port` en `application.yml`.
- Cambiar nombre registrado en Eureka: editar `spring.application.name`.
- Cambiar comunicacion con otro microservicio: editar clases en `client/` que usan `@FeignClient`.

Base de datos de este servicio:

- Nombre: `playgg_auth_db`
- Puerto del servicio: `8082`
- Archivo de configuracion: `src/main/resources/application.yml`

Conceptos Spring Boot usados:

- `@RestController`: indica que la clase expone endpoints REST.
- `@RequestMapping`: define la ruta base del controller.
- `@GetMapping`: consulta datos.
- `@PostMapping`: crea datos.
- `@PutMapping`: actualiza datos.
- `@DeleteMapping`: elimina datos.
- `@Valid`: activa validaciones del DTO.
- `@RequestBody`: recibe JSON desde el body.
- `@PathVariable`: toma datos desde la URL, por ejemplo el id.
- `@Service`: marca la clase de logica de negocio.
- `@Repository`: capa de acceso a datos, normalmente extendiendo `JpaRepository`.
- `@Entity`: indica que una clase representa una tabla.
- `@FeignClient`: declara un cliente HTTP para comunicarse con otro microservicio.

## Conexion MySQL / XAMPP

Este servicio puede usar MySQL desde XAMPP.

1. Iniciar MySQL en XAMPP.
2. Crear la base de datos `playgg_auth_db` en phpMyAdmin o dejar que Spring la cree con `createDatabaseIfNotExist=true`.
3. Revisar `src/main/resources/application.yml`.
4. Si XAMPP usa usuario `root` sin password, dejar:

```yml
spring:
  datasource:
    username: root
    password:
```

Si tu MySQL tiene password, escribirla en `password`.
