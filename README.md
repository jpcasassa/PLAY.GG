# PLAY.GG Microservices

Proyecto academico de arquitectura de microservicios para una plataforma gamer/social. Esta version busca ser limpia, modular y facil de entender.

## Arquitectura

Se usa Maven Multi-Module con un POM padre que centraliza Java 21, Spring Boot 3 y Spring Cloud. Cada servicio mantiene arquitectura CSR: Controller recibe HTTP, Service aplica reglas de negocio y Repository accede a MySQL con JPA.

Los DTOs separan la API de las entidades JPA. El flujo principal es Controller -> Service -> Repository, manteniendo responsabilidades claras.

## Microservicios

| Servicio | Puerto | Responsabilidad |
| --- | ---: | --- |
| [discovery-service](discovery-service/README.md) | 8761 | Eureka Server |
| [gateway-service](gateway-service/README.md) | 8080 | API Gateway |
| [user-service](user-service/README.md) | 8081 | Usuarios |
| [auth-service](auth-service/README.md) | 8082 | JWT y sesiones |
| [profile-service](profile-service/README.md) | 8083 | Perfil gamer/social |
| [forum-service](forum-service/README.md) | 8084 | Posts y comentarios |
| [community-service](community-service/README.md) | 8085 | Comunidades y miembros |
| [chat-service](chat-service/README.md) | 8086 | Mensajes privados |
| [notification-service](notification-service/README.md) | 8087 | Notificaciones |
| [game-service](game-service/README.md) | 8088 | Catalogo de juegos |
| [collectible-service](collectible-service/README.md) | 8089 | Logros y trofeos |
| [search-service](search-service/README.md) | 8090 | Historial de busquedas |

Cada microservicio tiene su propio README con explicacion de utilidad, datos que maneja, endpoints y puntos de apoyo.

## Tecnologias

Java 21, Spring Boot 3, Maven, Spring Cloud, Eureka, Spring Cloud Gateway, OpenFeign, MySQL, JPA/Hibernate, Bean Validation, Lombok, SLF4J y JWT.

## Seguridad

`auth-service` incluye Spring Security con autenticacion HTTP Basic simple para demostrar proteccion de endpoints.

- `/auth/register` y `/auth/login` son publicos.
- Los otros endpoints de `auth-service` requieren Basic Auth.
- Usuario de prueba: `admin`
- Password de prueba: `admin123`

Esta  configuracion es intencionalmente simple. Sirve para explicar el concepto de autenticacion basica sin agregar complejidad innecesaria.

## Ejecucion

1. Ajustar usuario/password de MySQL en cada application.yml si corresponde.
2. Compilar: `mvn clean package -DskipTests`.
3. Levantar primero `discovery-service`, luego `gateway-service` y despues los servicios de dominio.

```bash
mvn -pl discovery-service spring-boot:run
mvn -pl gateway-service spring-boot:run
mvn -pl user-service spring-boot:run
```

## Conexion a base de datos con XAMPP

Lo importante es usar el modulo MySQL/MariaDB que viene con XAMPP y configurar los `application.yml` de cada microservicio.

Pasos recomendados:

1. Abrir XAMPP Control Panel.
2. Iniciar `MySQL`.
3. Entrar a phpMyAdmin: `http://localhost/phpmyadmin`.
4. Crear las bases de datos o dejar que Spring las cree si el usuario tiene permisos.
5. Revisar usuario y password en cada `application.yml`.

Bases de datos usadas por el proyecto:

```sql
CREATE DATABASE IF NOT EXISTS playgg_users_db;
CREATE DATABASE IF NOT EXISTS playgg_auth_db;
CREATE DATABASE IF NOT EXISTS playgg_profiles_db;
CREATE DATABASE IF NOT EXISTS playgg_forum_db;
CREATE DATABASE IF NOT EXISTS playgg_communities_db;
CREATE DATABASE IF NOT EXISTS playgg_chat_db;
CREATE DATABASE IF NOT EXISTS playgg_notifications_db;
CREATE DATABASE IF NOT EXISTS playgg_games_db;
CREATE DATABASE IF NOT EXISTS playgg_collectibles_db;
CREATE DATABASE IF NOT EXISTS playgg_search_db;
```

Configuracion usada en los servicios:

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/nombre_de_la_base?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root
    password:
```

En XAMPP normalmente el usuario es `root` y la password viene vacia. Si configuraste password en MySQL, debes escribirla en `password`.

Archivos que se editan para cambiar la conexion:

- `user-service/src/main/resources/application.yml`
- `auth-service/src/main/resources/application.yml`
- `profile-service/src/main/resources/application.yml`
- `forum-service/src/main/resources/application.yml`
- `community-service/src/main/resources/application.yml`
- `chat-service/src/main/resources/application.yml`
- `notification-service/src/main/resources/application.yml`
- `game-service/src/main/resources/application.yml`
- `collectible-service/src/main/resources/application.yml`
- `search-service/src/main/resources/application.yml`

`discovery-service` y `gateway-service` no usan MySQL.

Orden recomendado para levantar el sistema:

1. `discovery-service`
2. `gateway-service`
3. `user-service`
4. `game-service`
5. `auth-service`
6. Los demas servicios segun lo que se quiera probar

Ejemplo:

```bash
mvn -pl discovery-service spring-boot:run
mvn -pl gateway-service spring-boot:run
mvn -pl user-service spring-boot:run
```

Si un servicio falla al iniciar por base de datos, revisar:

- Que MySQL de XAMPP este iniciado.
- Que el puerto `3306` no este ocupado por otro MySQL.
- Que la base de datos exista.
- Que usuario y password coincidan.
- Que el nombre de la base en la URL sea correcto.

## Endpoints principales

- `POST /users`
- `GET /users`
- `GET /users/{id}`
- `GET /users/nickname/{nickname}`
- `GET /users/email/{email}`
- `POST /auth/login`
- `POST /auth/register`
- `POST /profiles`
- `POST /posts`
- `POST /comments`
- `POST /communities/{id}/members`
- `POST /games`

## Regla importante

No hay relaciones JPA entre microservicios. `profile-service` guarda `userId` y `favoriteGameId`, y consulta datos externos via Feign cuando lo necesite. Esto conserva independencia por base de datos.

## Estructura interna

```text
src/main/java/com/playgg/[servicio]
|-- controller
|-- service
|-- repository
|-- model
|-- dto
|-- config
|-- client
|-- exception
|-- util
```

Esta estructura facilita explicar separacion de responsabilidades y el flujo Controller -> Service -> Repository.

## Como leer el codigo

Cada microservicio tiene su propio README con una guia especifica. Aun asi, la lectura general es la misma:

1. Abrir el `README.md` del microservicio.
2. Revisar `application.yml` para ver puerto, nombre del servicio, Eureka y base de datos.
3. Entrar al `controller` para ver los endpoints disponibles.
4. Seguir hacia `service` para entender la logica.
5. Revisar `repository` para ver como se accede a MySQL.
6. Revisar `model` para ver que campos se guardan.
7. Revisar `dto` para ver que datos entran y salen por la API.
8. Revisar `client` si el servicio se comunica con otros microservicios mediante Feign.

Donde editar segun lo que se quiera cambiar:

- Cambiar un endpoint: `controller`.
- Cambiar una validacion o regla de negocio: `service` o `dto`.
- Cambiar una tabla o campo guardado: `model`, `dto` y `service`.
- Cambiar una consulta de base de datos: `repository`.
- Cambiar conexion MySQL: `application.yml`.
- Cambiar comunicacion entre microservicios: `client`.
- Cambiar manejo de errores: `exception/GlobalExceptionHandler.java`.
- Cambiar seguridad basica: `auth-service/src/main/java/com/playgg/auth/config/SecurityConfig.java`.

## Apoyo para defensa

Ideas clave para explicar el proyecto:

- El sistema esta separado por dominios: usuarios, autenticacion, perfiles, foro, comunidades, chat, notificaciones, juegos, coleccionables y busqueda.
- Cada microservicio tiene su propia base de datos para reducir acoplamiento.
- No se crean relaciones JPA entre servicios. Se guardan ids como `userId` o `gameId` y, cuando se necesita mas informacion, se consulta con Feign.
- Eureka permite descubrir servicios por nombre.
- Gateway centraliza el acceso externo.
- DTOs separan los datos de entrada/salida de las entidades internas.
- `@ControllerAdvice` centraliza errores y evita repetir manejo de excepciones.
- Spring Security en `auth-service` demuestra autenticacion basica con HTTP Basic.
- Es una version universitaria funcional y preparada para crecer, sin agregar tecnologias innecesarias como Kafka, RabbitMQ, Kubernetes o WebSockets.
