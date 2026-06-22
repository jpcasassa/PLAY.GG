# game-service

## Para que sirve

Administra el catalogo de videojuegos de PLAY.GG.

## Utilidad en el proyecto

Permite que otros servicios usen juegos como referencia, por ejemplo perfiles con juego favorito o coleccionables asociados a un juego.

## Datos que maneja

Entidad `Game`:

- `gameId`
- `title`
- `genre`
- `platform`
- `multiplayer`
- `competitive`
- `imageUrl`

## Base de datos

`playgg_games_db`

## Puerto

`8088`

## Endpoints

- `POST /games`
- `GET /games`
- `GET /games/{id}`
- `PUT /games/{id}`
- `DELETE /games/{id}`

## Datos que pide

```json
{
  "title": "Valorant",
  "genre": "Shooter",
  "platform": "PC",
  "multiplayer": true,
  "competitive": true,
  "imageUrl": "https://example.com/valorant.jpg"
}
```

## Resumen

Este servicio representa un catalogo independiente. Otros servicios guardan `gameId`, pero no crean relaciones JPA con `Game`.

## Guia para leer el codigo

Este microservicio sigue arquitectura CSR: Controller -> Service -> Repository.

Archivos principales:

- `src/main/java/com/playgg/game/GameServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/game/controller/GameController.java`: recibe las peticiones HTTP con `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.
- `src/main/java/com/playgg/game/service/GameService.java`: contiene la logica de negocio, validaciones de existencia, conversion a DTO y logs.
- `src/main/java/com/playgg/game/repository/GameRepository.java`: conecta con la base de datos usando `JpaRepository`.
- `src/main/java/com/playgg/game/model/`: contiene las entidades JPA. En este servicio la entidad principal es `Game`.
- `src/main/java/com/playgg/game/dto/`: contiene los objetos de entrada y salida. Sirven para no exponer directamente la entidad.
- `src/main/java/com/playgg/game/exception/GlobalExceptionHandler.java`: centraliza errores 400, 404 y 500.
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

- Nombre: `playgg_games_db`
- Puerto del servicio: `8088`
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
2. Crear la base de datos `playgg_games_db` en phpMyAdmin o dejar que Spring la cree con `createDatabaseIfNotExist=true`.
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
http://localhost:8080/games
```

Tambien se puede probar directo si el servicio esta levantado:

```text
http://localhost:8088/games
```

En Thunder Client seleccionar `Body > JSON` y enviar:

```json
{
  "title": "Valorant",
  "genre": "Shooter",
  "platform": "PC",
  "multiplayer": true,
  "competitive": true,
  "imageUrl": "https://example.com/valorant.jpg"
}
```

Peticiones utiles:

- `POST http://localhost:8080/games`
- `GET http://localhost:8080/games`
- `GET http://localhost:8080/games/1`
- `PUT http://localhost:8080/games/1`
- `DELETE http://localhost:8080/games/1`
