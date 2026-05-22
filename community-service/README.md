# community-service

## Para que sirve

Gestiona comunidades, clanes o squads dentro de PLAY.GG.

## Utilidad en el proyecto

Permite crear grupos de usuarios con roles internos como owner, moderador o miembro.

## Datos que maneja

Entidad `Community`:

- `communityId`
- `ownerId`
- `name`
- `description`
- `bannerUrl`
- `createdAt`
- `active`

Entidad `CommunityMember`:

- `memberId`
- `communityId`
- `userId`
- `joinedAt`
- `role`

Roles:

- `OWNER`
- `MODERATOR`
- `MEMBER`

## Base de datos

`playgg_communities_db`

## Puerto

`8085`

## Endpoints

- `POST /communities`
- `GET /communities`
- `GET /communities/{id}`
- `PUT /communities/{id}`
- `DELETE /communities/{id}`
- `POST /communities/{id}/members`
- `GET /communities/{id}/members`

## Datos combinados

Guarda `ownerId` y `userId`, pero no crea relacion JPA con usuarios. Si necesita datos del usuario, consulta `user-service`.

## Resumen

Este servicio maneja comunidades y miembros. La relacion JPA existe entre entidades internas del mismo servicio, no entre servicios distintos.

## Guia para leer el codigo

Este microservicio sigue arquitectura CSR: Controller -> Service -> Repository.

Archivos principales:

- `src/main/java/com/playgg/community/CommunityServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/community/controller/CommunityController.java`: recibe las peticiones HTTP con `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.
- `src/main/java/com/playgg/community/service/CommunityService.java`: contiene la logica de negocio, validaciones de existencia, conversion a DTO y logs.
- `src/main/java/com/playgg/community/repository/CommunityRepository / CommunityMemberRepository.java`: conecta con la base de datos usando `JpaRepository`.
- `src/main/java/com/playgg/community/model/`: contiene las entidades JPA. En este servicio la entidad principal es `Community / CommunityMember`.
- `src/main/java/com/playgg/community/dto/`: contiene los objetos de entrada y salida. Sirven para no exponer directamente la entidad.
- `src/main/java/com/playgg/community/exception/GlobalExceptionHandler.java`: centraliza errores 400, 404 y 500.
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

- Nombre: `playgg_communities_db`
- Puerto del servicio: `8085`
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
2. Crear la base de datos `playgg_communities_db` en phpMyAdmin o dejar que Spring la cree con `createDatabaseIfNotExist=true`.
3. Revisar `src/main/resources/application.yml`.
4. Si XAMPP usa usuario `root` sin password, dejar:

```yml
spring:
  datasource:
    username: root
    password:
```

Si tu MySQL tiene password, escribirla en `password`.
