# profile-service

## Para que sirve

Administra el perfil gamer y social de cada usuario.

## Utilidad en el proyecto

Permite guardar informacion que no pertenece directamente al usuario base, como avatar, biografia, cuenta de Steam o Discord, nivel y juego favorito.

## Datos que maneja

Entidad `Profile`:

- `profileId`
- `userId`
- `avatarUrl`
- `bannerUrl`
- `bio`
- `steamUsername`
- `discordUsername`
- `favoriteGameId`
- `rank`
- `level`
- `createdAt`
- `updatedAt`

## Base de datos

`playgg_profiles_db`

## Puerto

`8083`

## Endpoints

- `POST /profiles`
- `GET /profiles`
- `GET /profiles/{id}`
- `PUT /profiles/{id}`
- `DELETE /profiles/{id}`

## Datos combinados

Este servicio guarda `userId` y `favoriteGameId`. Si necesita datos completos, los consulta con Feign a:

- `user-service`: nickname, pais, usuario.
- `game-service`: titulo, genero, juego favorito.

## Resumen

No existe `@OneToOne User`; solo se guarda el id del usuario. Esto evita acoplar bases de datos.

## Guia para leer el codigo

Este microservicio sigue arquitectura CSR: Controller -> Service -> Repository.

Archivos principales:

- `src/main/java/com/playgg/profile/ProfileServiceApplication.java`: clase que inicia Spring Boot.
- `src/main/java/com/playgg/profile/controller/ProfileController.java`: recibe las peticiones HTTP con `@GetMapping`, `@PostMapping`, `@PutMapping` y `@DeleteMapping`.
- `src/main/java/com/playgg/profile/service/ProfileService.java`: contiene la logica de negocio, validaciones de existencia, conversion a DTO y logs.
- `src/main/java/com/playgg/profile/repository/ProfileRepository.java`: conecta con la base de datos usando `JpaRepository`.
- `src/main/java/com/playgg/profile/model/`: contiene las entidades JPA. En este servicio la entidad principal es `Profile`.
- `src/main/java/com/playgg/profile/dto/`: contiene los objetos de entrada y salida. Sirven para no exponer directamente la entidad.
- `src/main/java/com/playgg/profile/exception/GlobalExceptionHandler.java`: centraliza errores 400, 404 y 500.
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

- Nombre: `playgg_profiles_db`
- Puerto del servicio: `8083`
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
2. Crear la base de datos `playgg_profiles_db` en phpMyAdmin o dejar que Spring la cree con `createDatabaseIfNotExist=true`.
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
http://localhost:8080/profiles
```

Tambien se puede probar directo si el servicio esta levantado:

```text
http://localhost:8083/profiles
```

En Thunder Client seleccionar `Body > JSON` y enviar:

```json
{
  "userId": 1,
  "avatarUrl": "https://example.com/avatar.png",
  "bannerUrl": "https://example.com/banner.png",
  "bio": "Jugador competitivo de FPS",
  "steamUsername": "playerSteam",
  "discordUsername": "playerOne#1234",
  "favoriteGameId": 1,
  "rank": "Gold",
  "level": 12
}
```

Peticiones utiles:

- `POST http://localhost:8080/profiles`
- `GET http://localhost:8080/profiles`
- `GET http://localhost:8080/profiles/1`
- `PUT http://localhost:8080/profiles/1`
- `DELETE http://localhost:8080/profiles/1`

El `userId` debe existir en `user-service`. Si se envia `favoriteGameId`, tambien debe existir en `game-service`.

## Pruebas Unitarias

Este microservicio tiene pruebas unitarias con JUnit 5 y Mockito en `src/test/java/com/playgg/profile/service/ProfileServiceTest.java`.

La clase probada es `ProfileService`. Se mockean solo `ProfileRepository`, `UserClient` y `GameClient`, por lo que no se usa base de datos real ni llamadas HTTP reales.

Los tests cubren:

- Crear perfil correctamente.
- Buscar perfiles correctamente.
- Actualizar perfil correctamente.
- Eliminar perfil correctamente.
- Manejar errores cuando el perfil no existe.
- Validar que `favoriteGameId` opcional no llame a `game-service` cuando es nulo.

Mockito se usa para crear mocks, que son objetos simulados que reemplazan dependencias reales durante el test. En microservicios esto permite probar la logica de negocio de forma aislada.

Para ejecutar:

```bash
mvn test
```
