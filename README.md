# PLAY.GG Microservices

PLAY.GG es una plataforma gamer/social construida como proyecto universitario de microservicios con Spring Boot. La idea es separar responsabilidades por dominio: usuarios, autenticacion, perfiles, foro, comunidades, chat, notificaciones, juegos y coleccionables.

El objetivo de esta version es ser clara, defendible y facil de recorrer. No usa tecnologias de infraestructura avanzada como Kafka, RabbitMQ, Kubernetes, Redis, WebSockets ni Docker complejo.

## Arquitectura general

El proyecto usa Maven Multi-Module. El `pom.xml` padre centraliza Java 21, Spring Boot 3 y Spring Cloud para que todos los servicios trabajen con versiones coherentes.

Cada microservicio de dominio sigue una arquitectura CSR simple:

```text
controller -> service -> repository
```

- `controller`: recibe peticiones HTTP y valida DTOs de entrada.
- `service`: contiene reglas de negocio, validaciones y conversion entre entidades y DTOs.
- `repository`: accede a MySQL mediante Spring Data JPA.
- `model`: define entidades JPA propias del microservicio.
- `dto`: define datos de entrada y salida sin exponer directamente las entidades.
- `exception`: centraliza errores comunes de la API.
- `client`: aparece solo en servicios que consultan otro microservicio con OpenFeign.
- `config`: aparece solo cuando el servicio necesita configuracion real, como seguridad en `auth-service`.

## Microservicios

| Servicio | Puerto | Responsabilidad principal |
| --- | ---: | --- |
| `discovery-service` | 8761 | Registro Eureka de servicios |
| `gateway-service` | 8080 | Entrada unica a la API usando Spring Cloud Gateway |
| `user-service` | 8081 | Usuarios, datos de cuenta y datos internos para autenticacion |
| `auth-service` | 8082 | Registro, login, JWT, refresh token y logout |
| `profile-service` | 8083 | Perfil gamer/social del usuario |
| `forum-service` | 8084 | Posts y comentarios |
| `community-service` | 8085 | Comunidades y miembros |
| `chat-service` | 8086 | Mensajes privados simples |
| `notification-service` | 8087 | Notificaciones de usuario |
| `game-service` | 8088 | Catalogo de juegos |
| `collectible-service` | 8089 | Logros, trofeos o coleccionables |

El modulo de busquedas fue retirado para mantener la arquitectura mas simple y enfocada en los dominios principales del sistema.

## Comunicacion entre servicios

La comunicacion se mantiene reducida. Los servicios no se conectan todos contra todos.

| Servicio origen | Consulta permitida |
| --- | --- |
| `auth-service` | `user-service` |
| `profile-service` | `user-service`, `game-service` |
| `forum-service` | `user-service` |
| `community-service` | `user-service` |
| `chat-service` | `user-service` |
| `notification-service` | `user-service` |
| `collectible-service` | `user-service`, `game-service` |

Los microservicios no tienen relaciones JPA entre bases distintas. Por ejemplo, un post guarda `userId`, un perfil guarda `userId` y `favoriteGameId`, y un coleccionable guarda `userId` y `gameId`. Si se necesita comprobar que esos ids existen, se consulta al servicio correspondiente mediante Feign.

Las relaciones JPA se usan solo dentro del mismo microservicio. Ejemplos:

- `Post -> Comment` dentro de `forum-service`.
- `Community -> CommunityMember` dentro de `community-service`.

## Tecnologias utilizadas

- Java 21
- Spring Boot 3
- Spring Cloud
- Eureka Discovery Server
- Spring Cloud Gateway
- OpenFeign
- Spring Security en `auth-service`
- JWT con JJWT
- Spring Data JPA / Hibernate
- MySQL
- Bean Validation
- Lombok
- Maven Multi-Module

## Eureka y Gateway

Eureka funciona como registro de servicios. Cada microservicio se registra con su nombre, por ejemplo `user-service` o `forum-service`. Esto permite que otros servicios los encuentren por nombre sin depender de una URL fija.

Gateway es la puerta de entrada de la API. En vez de llamar directamente a cada puerto, el cliente puede entrar por `http://localhost:8080` y Gateway redirige al microservicio correcto usando Eureka.

## Feign

OpenFeign permite declarar clientes HTTP con interfaces Java. En este proyecto se usa para consultas simples entre servicios, principalmente para validar que un `userId` o `gameId` exista.

Ejemplo conceptual:

```java
@FeignClient(name = "user-service")
public interface UserClient {
  @GetMapping("/users/{id}")
  ResponseEntity<Object> findById(@PathVariable Long id);
}
```

Feign se usa solo donde aporta claridad. Los servicios que no consultan otros microservicios no tienen dependencia de OpenFeign.

## JWT

`auth-service` genera tokens JWT al registrar o iniciar sesion. El token contiene datos basicos como id de usuario, email y rol. Tambien se guarda una sesion en MySQL para poder manejar refresh token y logout.

Esta implementacion es simple y pensada para el contexto universitario: demuestra autenticacion, tokens y sesiones sin entrar en OAuth ni configuraciones empresariales complejas.

## Bases de datos

Cada microservicio de dominio usa su propia base de datos MySQL. Con XAMPP se puede iniciar MySQL/MariaDB y crear las bases desde phpMyAdmin, o dejar que Spring las cree si el usuario tiene permisos.

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
```

Si tu MySQL usa password, ajusta `spring.datasource.password` en el `application.yml` de cada servicio.

## Estructura del proyecto

```text
PLAY.GG/
|-- pom.xml
|-- discovery-service/
|-- gateway-service/
|-- user-service/
|-- auth-service/
|-- profile-service/
|-- forum-service/
|-- community-service/
|-- chat-service/
|-- notification-service/
|-- game-service/
|-- collectible-service/
```

Estructura base de un microservicio de dominio:

```text
src/main/java/com/playgg/[servicio]/
|-- controller
|-- service
|-- repository
|-- model
|-- dto
|-- exception
|-- client     # solo si consume otro servicio con Feign
|-- config     # solo si necesita configuracion propia
```

## Como ejecutar

1. Iniciar MySQL desde XAMPP.
2. Verificar usuario y password en los `application.yml`.
3. Compilar el proyecto completo:

```bash
mvn clean package -DskipTests
```

4. Levantar primero Eureka:

```bash
mvn -pl discovery-service spring-boot:run
```

5. Levantar Gateway:

```bash
mvn -pl gateway-service spring-boot:run
```

6. Levantar los servicios de dominio que se quieran probar:

```bash
mvn -pl user-service spring-boot:run
mvn -pl auth-service spring-boot:run
mvn -pl game-service spring-boot:run
```

## Endpoints principales

- `POST /users`
- `GET /users`
- `GET /users/{id}`
- `GET /users/nickname/{nickname}`
- `GET /users/email/{email}`
- `GET /users/internal/auth/email/{email}`
- `POST /auth/register`
- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`
- `POST /profiles`
- `POST /posts`
- `POST /comments`
- `POST /communities`
- `POST /communities/{id}/members`
- `POST /messages`
- `POST /notifications`
- `POST /games`
- `POST /collectibles`

## Ideas para defensa tecnica

- El sistema se divide por dominios para que cada servicio tenga una responsabilidad clara.
- Eureka permite descubrir servicios por nombre.
- Gateway centraliza el acceso externo.
- Controller, Service y Repository separan entrada HTTP, reglas de negocio y persistencia.
- Los DTOs evitan exponer entidades JPA directamente.
- Feign se usa solo para validar datos externos necesarios.
- No hay relaciones JPA entre microservicios; solo ids como `userId`, `gameId` o `postId`.
- Las relaciones JPA locales se mantienen donde ayudan a representar el dominio.
- JWT se concentra en `auth-service`, por lo que la autenticacion no queda repartida por todo el proyecto.