# user-service

## Para que sirve

Gestiona los usuarios de PLAY.GG. Es uno de los servicios principales porque otros modulos necesitan saber quien realiza acciones dentro de la plataforma.

## Utilidad en el proyecto

Permite crear, listar, buscar, actualizar y eliminar usuarios. Tambien entrega datos internos al `auth-service` para validar login.

## Datos que maneja

Entidad `User`:

- `userId`
- `nickname`
- `firstName`
- `lastName`
- `email`
- `password`
- `country`
- `role`
- `createdAt`
- `updatedAt`
- `active`

El `nickname` y el `email` son unicos. La password debe tener minimo 8 caracteres.

## Base de datos

`playgg_users_db`

## Puerto

`8081`

## Endpoints principales

- `POST /users`: crea un usuario.
- `GET /users`: lista usuarios.
- `GET /users/{id}`: busca por id.
- `PUT /users/{id}`: actualiza un usuario.
- `DELETE /users/{id}`: elimina un usuario.
- `GET /users/nickname/{nickname}`: busca por nickname.
- `GET /users/email/{email}`: busca por email sin exponer password.
- `GET /users/internal/auth/email/{email}`: endpoint interno para auth.

## Ejemplo de creacion

```json
{
  "nickname": "playerOne",
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan@mail.com",
  "password": "12345678",
  "country": "Chile",
  "role": "PLAYER"
}
```

## Resumen

Este servicio maneja CRUD, DTOs, validaciones, JPA y separacion CSR. Otros servicios no tienen relacion JPA con `User`; solo guardan `userId` y consultan por Feign si necesitan mas informacion.

## Guia para leer el codigo

Este microservicio sigue arquitectura CSR: Controller -> Service -> Repository.

Archivos principales:

- `src/main/java/com/playgg/user/UserServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/user/controller/UserController.java`: recibe las peticiones HTTP con `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.
- `src/main/java/com/playgg/user/service/UserService.java`: contiene la logica de negocio, validaciones de existencia, conversion a DTO y logs.
- `src/main/java/com/playgg/user/repository/UserRepository.java`: conecta con la base de datos usando `JpaRepository`.
- `src/main/java/com/playgg/user/model/`: contiene las entidades JPA. En este servicio la entidad principal es `User`.
- `src/main/java/com/playgg/user/dto/`: contiene los objetos de entrada y salida. Sirven para no exponer directamente la entidad.
- `src/main/java/com/playgg/user/exception/GlobalExceptionHandler.java`: centraliza errores 400, 404 y 500.
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

- Nombre: `playgg_users_db`
- Puerto del servicio: `8081`
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
2. Crear la base de datos `playgg_users_db` en phpMyAdmin o dejar que Spring la cree con `createDatabaseIfNotExist=true`.
3. Revisar `src/main/resources/application.yml`.
4. Si XAMPP usa usuario `root` sin password, dejar:

```yml
spring:
  datasource:
    username: root
    password:
```

Si tu MySQL tiene password, escribirla en `password`.
