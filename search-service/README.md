# search-service

## Para que sirve

Registra busquedas realizadas dentro de PLAY.GG.

## Utilidad en el proyecto

Sirve como base para una busqueda global de usuarios, juegos, posts y comunidades. En esta version inicial se implementa un historial de busqueda simple y extendible.

## Datos que maneja

Entidad `SearchHistory`:

- `searchId`
- `userId`
- `query`
- `searchedAt`

## Base de datos

`playgg_search_db`

## Puerto

`8090`

## Endpoints

- `POST /search-history`
- `GET /search-history`
- `GET /search-history/{id}`
- `PUT /search-history/{id}`
- `DELETE /search-history/{id}`

## Comunicacion con otros servicios

Tiene clientes Feign preparados para consultar:

- `user-service`
- `game-service`
- `forum-service`
- `community-service`

## Para la defensa

Este servicio se puede presentar como una primera version de busqueda global. Su valor es registrar consultas y dejar preparada la comunicacion para buscar en distintos dominios.

## Guia para leer el codigo

Este microservicio sigue arquitectura CSR: Controller -> Service -> Repository.

Archivos principales:

- `src/main/java/com/playgg/search/SearchServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/search/controller/SearchHistoryController.java`: recibe las peticiones HTTP con `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.
- `src/main/java/com/playgg/search/service/SearchHistoryService.java`: contiene la logica de negocio, validaciones de existencia, conversion a DTO y logs.
- `src/main/java/com/playgg/search/repository/SearchHistoryRepository.java`: conecta con la base de datos usando `JpaRepository`.
- `src/main/java/com/playgg/search/model/`: contiene las entidades JPA. En este servicio la entidad principal es `SearchHistory`.
- `src/main/java/com/playgg/search/dto/`: contiene los objetos de entrada y salida. Sirven para no exponer directamente la entidad.
- `src/main/java/com/playgg/search/exception/GlobalExceptionHandler.java`: centraliza errores 400, 404 y 500.
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

- Nombre: `playgg_search_db`
- Puerto del servicio: `8090`
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
2. Crear la base de datos `playgg_search_db` en phpMyAdmin o dejar que Spring la cree con `createDatabaseIfNotExist=true`.
3. Revisar `src/main/resources/application.yml`.
4. Si XAMPP usa usuario `root` sin password, dejar:

```yml
spring:
  datasource:
    username: root
    password:
```

Si tu MySQL tiene password, escribirla en `password`.
