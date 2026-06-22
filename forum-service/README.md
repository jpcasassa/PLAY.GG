# forum-service

## Para que sirve

Gestiona publicaciones y comentarios del foro de PLAY.GG.

## Utilidad en el proyecto

Permite que usuarios creen posts sobre juegos, comunidades o temas generales, y que otros usuarios comenten.

## Datos que maneja

Entidad `Post`:

- `postId`
- `userId`
- `title`
- `content`
- `category`
- `likes`
- `createdAt`
- `updatedAt`

Entidad `Comment`:

- `commentId`
- `postId`
- `userId`
- `content`
- `createdAt`

## Relacion JPA interna

Este servicio usa una relacion `OneToMany` entre `Post` y `Comment`, porque ambas entidades pertenecen al mismo microservicio y a la misma base de datos.

## Base de datos

`playgg_forum_db`

## Puerto

`8084`

## Endpoints

- `POST /posts`
- `GET /posts`
- `GET /posts/{id}`
- `PUT /posts/{id}`
- `DELETE /posts/{id}`
- `POST /comments`
- `GET /comments/post/{postId}`

## Comunicacion con otros servicios

- Consulta `user-service` por Feign para validar que el `userId` del autor o comentarista exista.
- No crea notificaciones automaticamente en el codigo actual. Si se quisiera agregar ese flujo, habria que incorporar un cliente Feign hacia `notification-service`.

## Resumen

Las relaciones JPA solo se usan dentro del mismo microservicio. `Post` y `Comment` si pueden relacionarse porque comparten responsabilidad y base de datos.

## Guia para leer el codigo

Este microservicio sigue arquitectura CSR: Controller -> Service -> Repository.

Archivos principales:

- `src/main/java/com/playgg/forum/ForumServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/forum/controller/PostController / CommentController.java`: recibe las peticiones HTTP con `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.
- `src/main/java/com/playgg/forum/service/PostService / CommentService.java`: contiene la logica de negocio, validaciones de existencia, conversion a DTO y logs.
- `src/main/java/com/playgg/forum/repository/PostRepository / CommentRepository.java`: conecta con la base de datos usando `JpaRepository`.
- `src/main/java/com/playgg/forum/model/`: contiene las entidades JPA. En este servicio la entidad principal es `Post / Comment`.
- `src/main/java/com/playgg/forum/dto/`: contiene los objetos de entrada y salida. Sirven para no exponer directamente la entidad.
- `src/main/java/com/playgg/forum/exception/GlobalExceptionHandler.java`: centraliza errores 400, 404 y 500.
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

- Nombre: `playgg_forum_db`
- Puerto del servicio: `8084`
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
2. Crear la base de datos `playgg_forum_db` en phpMyAdmin o dejar que Spring la cree con `createDatabaseIfNotExist=true`.
3. Revisar `src/main/resources/application.yml`.
4. Si XAMPP usa usuario `root` sin password, dejar:

```yml
spring:
  datasource:
    username: root
    password:
```

Si tu MySQL tiene password, escribirla en `password`.

## Pruebas con Thunder Client

Usar preferentemente Gateway:

```text
http://localhost:8080/posts
http://localhost:8080/comments
```

Tambien se puede probar directo si el servicio esta levantado:

```text
http://localhost:8084/posts
http://localhost:8084/comments
```

Crear post:

```text
POST http://localhost:8080/posts
```

```json
{
  "userId": 1,
  "title": "Busco squad para ranked",
  "content": "Juego de noche y busco equipo para subir rango.",
  "category": "Valorant"
}
```

Crear comentario:

```text
POST http://localhost:8080/comments
```

```json
{
  "postId": 1,
  "userId": 1,
  "content": "Me sumo, juego support."
}
```

Peticiones utiles:

- `GET http://localhost:8080/posts`
- `GET http://localhost:8080/posts/1`
- `PUT http://localhost:8080/posts/1`
- `DELETE http://localhost:8080/posts/1`
- `GET http://localhost:8080/comments/post/1`

El `userId` debe existir en `user-service`. Para comentar, el `postId` debe existir en `forum-service`.
