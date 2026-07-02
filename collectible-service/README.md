# collectible-service

## Para que sirve

Gestiona logros, trofeos o coleccionables obtenidos por usuarios.

## Utilidad en el proyecto

Permite registrar recompensas asociadas a un usuario y a un juego.

## Datos que maneja

Entidad `Collectible`:

- `collectibleId`
- `userId`
- `gameId`
- `name`
- `description`
- `rarity`
- `unlockedAt`

Rarezas:

- `COMMON`
- `RARE`
- `EPIC`
- `LEGENDARY`

## Base de datos

`playgg_collectibles_db`

## Puerto

`8089`

## Endpoints

- `POST /collectibles`
- `GET /collectibles`
- `GET /collectibles/{id}`
- `PUT /collectibles/{id}`
- `DELETE /collectibles/{id}`

## Datos combinados

Guarda `userId` y `gameId`. Puede consultar:

- `user-service` para datos del jugador.
- `game-service` para datos del juego.

## Resumen

Este servicio usa ids externos sin acoplarse directamente a tablas de otros servicios.

## Guia para leer el codigo

Este microservicio sigue arquitectura CSR: Controller -> Service -> Repository.

Archivos principales:

- `src/main/java/com/playgg/collectible/CollectibleServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/collectible/controller/CollectibleController.java`: recibe las peticiones HTTP con `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.
- `src/main/java/com/playgg/collectible/service/CollectibleService.java`: contiene la logica de negocio, validaciones de existencia, conversion a DTO y logs.
- `src/main/java/com/playgg/collectible/repository/CollectibleRepository.java`: conecta con la base de datos usando `JpaRepository`.
- `src/main/java/com/playgg/collectible/model/`: contiene las entidades JPA. En este servicio la entidad principal es `Collectible`.
- `src/main/java/com/playgg/collectible/dto/`: contiene los objetos de entrada y salida. Sirven para no exponer directamente la entidad.
- `src/main/java/com/playgg/collectible/exception/GlobalExceptionHandler.java`: centraliza errores 400, 404 y 500.
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

- Nombre: `playgg_collectibles_db`
- Puerto del servicio: `8089`
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
2. Crear la base de datos `playgg_collectibles_db` en phpMyAdmin o dejar que Spring la cree con `createDatabaseIfNotExist=true`.
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
http://localhost:8080/collectibles
```

Tambien se puede probar directo si el servicio esta levantado:

```text
http://localhost:8089/collectibles
```

En Thunder Client seleccionar `Body > JSON` y enviar:

```json
{
  "userId": 1,
  "gameId": 1,
  "name": "Primera victoria",
  "description": "Ganar la primera partida competitiva",
  "rarity": "RARE"
}
```

Peticiones utiles:

- `POST http://localhost:8080/collectibles`
- `GET http://localhost:8080/collectibles`
- `GET http://localhost:8080/collectibles/1`
- `PUT http://localhost:8080/collectibles/1`
- `DELETE http://localhost:8080/collectibles/1`

El `userId` debe existir en `user-service` y el `gameId` en `game-service`. Rarezas validas: `COMMON`, `RARE`, `EPIC`, `LEGENDARY`.

## Pruebas con Swagger

Este servicio tambien se puede probar desde Swagger UI.

1. Levantar `discovery-service`.
2. Levantar `user-service` y `game-service` si se van a validar ids externos.
3. Levantar `collectible-service`.
4. Abrir:

```text
http://localhost:8089/swagger-ui.html
```

Tambien se puede ver la especificacion OpenAPI en:

```text
http://localhost:8089/v3/api-docs
```

En Swagger abrir el endpoint, presionar `Try it out`, completar el JSON cuando corresponda y ejecutar con `Execute`.

## Pruebas Unitarias

Este microservicio tiene pruebas unitarias con JUnit 5 y Mockito en `src/test/java/com/playgg/collectible/service/CollectibleServiceTest.java`.

La clase probada es `CollectibleService`. Se mockean solo `CollectibleRepository`, `UserClient` y `GameClient`, por lo que no se usa base de datos real ni llamadas HTTP reales.

Los tests cubren:

- Crear coleccionable correctamente.
- Buscar coleccionables correctamente.
- Actualizar coleccionable correctamente.
- Eliminar coleccionable correctamente.
- Manejar errores cuando el coleccionable no existe.
- Validar usuario y juego mediante mocks de Feign.

Mockito se usa para crear mocks, que son objetos simulados que reemplazan dependencias reales durante el test. En microservicios esto permite probar la logica de negocio de forma aislada.

Para ejecutar:

```bash
mvn test
```
