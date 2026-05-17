# chat-service

## Para que sirve

Gestiona mensajes privados entre usuarios.

## Utilidad en el proyecto

Permite registrar mensajes enviados de un usuario a otro. No usa WebSockets porque el alcance universitario del proyecto se mantiene simple y enfocado en CRUD REST.

## Datos que maneja

Entidad `Message`:

- `messageId`
- `senderId`
- `receiverId`
- `content`
- `sentAt`
- `read`

## Base de datos

`playgg_chat_db`

## Puerto

`8086`

## Endpoints

- `POST /messages`
- `GET /messages`
- `GET /messages/{id}`
- `PUT /messages/{id}`
- `DELETE /messages/{id}`

## Datos que pide

```json
{
  "senderId": 1,
  "receiverId": 2,
  "content": "Hola, jugamos?"
}
```

## Comunicacion con otros servicios

- Puede consultar `user-service` para validar usuarios.
- Puede conectarse con `notification-service` para avisar mensajes nuevos.

## Para la defensa

El chat se implementa como mensajeria REST simple. Esto cumple el objetivo sin agregar WebSockets ni complejidad innecesaria.

## Guia para leer el codigo

Este microservicio sigue arquitectura CSR: Controller -> Service -> Repository.

Archivos principales:

- `src/main/java/com/playgg/chat/ChatServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/chat/controller/MessageController.java`: recibe las peticiones HTTP con `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.
- `src/main/java/com/playgg/chat/service/MessageService.java`: contiene la logica de negocio, validaciones de existencia, conversion a DTO y logs.
- `src/main/java/com/playgg/chat/repository/MessageRepository.java`: conecta con la base de datos usando `JpaRepository`.
- `src/main/java/com/playgg/chat/model/`: contiene las entidades JPA. En este servicio la entidad principal es `Message`.
- `src/main/java/com/playgg/chat/dto/`: contiene los objetos de entrada y salida. Sirven para no exponer directamente la entidad.
- `src/main/java/com/playgg/chat/exception/GlobalExceptionHandler.java`: centraliza errores 400, 404 y 500.
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

- Nombre: `playgg_chat_db`
- Puerto del servicio: `8086`
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
2. Crear la base de datos `playgg_chat_db` en phpMyAdmin o dejar que Spring la cree con `createDatabaseIfNotExist=true`.
3. Revisar `src/main/resources/application.yml`.
4. Si XAMPP usa usuario `root` sin password, dejar:

```yml
spring:
  datasource:
    username: root
    password:
```

Si tu MySQL tiene password, escribirla en `password`.
