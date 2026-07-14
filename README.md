# PLAY.GG Microservices .

PLAY.GG es una plataforma gamer/social construida como proyecto universitario de microservicios con Spring Boot. La idea es separar responsabilidades por dominio: usuarios, autenticacion, perfiles, foro, comunidades, chat, notificaciones, juegos y coleccionables.

El objetivo de esta version es ser clara. No usa tecnologias de infraestructura avanzada.

## Arquitectura general

El proyecto usa Maven Multi-Module. El `pom.xml` padre centraliza Java 17, Spring Boot 3 y Spring Cloud para que todos los servicios trabajen con versiones coherentes.

El diagrama completo de arquitectura esta en [`DIAGRAMA_ARQUITECTURA_MICROSERVICIOS.md`](DIAGRAMA_ARQUITECTURA_MICROSERVICIOS.md).

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
| `auth-service` | 8082 | Registro y login basico consultando `user-service` |
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

- Java 17
- Spring Boot 3
- Spring Cloud
- Eureka Discovery Server
- Spring Cloud Gateway
- OpenFeign
- Spring Security en `auth-service`
- Spring Data JPA / Hibernate
- MySQL
- Bean Validation
- Springdoc OpenAPI / Swagger UI
- Lombok
- Maven Multi-Module

## Docker

Docker es una herramienta que permite empaquetar una aplicacion con su entorno de ejecucion en una imagen. Esa imagen se ejecuta como un contenedor, que es un proceso aislado y reproducible. En este proyecto sirve para levantar cada microservicio con Java 17 sin depender de tener Java o Maven configurados manualmente en la maquina donde se ejecuta.

Cada microservicio tiene su propio `Dockerfile` y su propio `.dockerignore`. Los Dockerfile usan una construccion multi-stage:

- una etapa Maven con `maven:3.9.9-eclipse-temurin-17` para compilar el JAR;
- una etapa runtime con `eclipse-temurin:17-jre` para ejecutar el microservicio.

Los comandos se ejecutan desde la raiz del proyecto `PLAY.GG`, porque cada modulo necesita resolver el `pom.xml` padre.

### Construir imagenes Docker

```bash
docker build -f discovery-service/Dockerfile -t playgg/discovery-service:latest .
docker build -f gateway-service/Dockerfile -t playgg/gateway-service:latest .
docker build -f user-service/Dockerfile -t playgg/user-service:latest .
docker build -f auth-service/Dockerfile -t playgg/auth-service:latest .
docker build -f profile-service/Dockerfile -t playgg/profile-service:latest .
docker build -f forum-service/Dockerfile -t playgg/forum-service:latest .
docker build -f community-service/Dockerfile -t playgg/community-service:latest .
docker build -f chat-service/Dockerfile -t playgg/chat-service:latest .
docker build -f notification-service/Dockerfile -t playgg/notification-service:latest .
docker build -f game-service/Dockerfile -t playgg/game-service:latest .
docker build -f collectible-service/Dockerfile -t playgg/collectible-service:latest .
```

Para construir todas las imagenes en PowerShell:

```powershell
$services = @(
  "discovery-service",
  "gateway-service",
  "user-service",
  "auth-service",
  "profile-service",
  "forum-service",
  "community-service",
  "chat-service",
  "notification-service",
  "game-service",
  "collectible-service"
)

foreach ($service in $services) {
  docker build -f "$service/Dockerfile" -t "playgg/${service}:latest" .
}
```

### Ejecutar un microservicio individual con Docker

Cada contenedor expone el mismo puerto configurado en su `application.yml`. Ejemplos:

```bash
docker run --name discovery-service --rm -p 8761:8761 playgg/discovery-service:latest
docker run --name gateway-service --rm -p 8080:8080 playgg/gateway-service:latest
docker run --name user-service --rm -p 8081:8081 playgg/user-service:latest
docker run --name auth-service --rm -p 8082:8082 playgg/auth-service:latest
docker run --name profile-service --rm -p 8083:8083 playgg/profile-service:latest
docker run --name forum-service --rm -p 8084:8084 playgg/forum-service:latest
docker run --name community-service --rm -p 8085:8085 playgg/community-service:latest
docker run --name chat-service --rm -p 8086:8086 playgg/chat-service:latest
docker run --name notification-service --rm -p 8087:8087 playgg/notification-service:latest
docker run --name game-service --rm -p 8088:8088 playgg/game-service:latest
docker run --name collectible-service --rm -p 8089:8089 playgg/collectible-service:latest
```

Si el microservicio necesita conectarse a MySQL instalado en la maquina anfitriona, en Docker Desktop se puede usar `host.docker.internal` como host de base de datos. Por ejemplo:

```bash
docker run --name user-service --rm -p 8081:8081 ^
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/playgg_users_db?createDatabaseIfNotExist=true ^
  -e SPRING_DATASOURCE_USERNAME=root ^
  -e SPRING_DATASOURCE_PASSWORD= ^
  playgg/user-service:latest
```

Para que un servicio se registre en Eureka ejecutado tambien en Docker, primero levanta `discovery-service` y luego pasa la URL de Eureka al servicio:

```bash
docker run --name discovery-service --rm -p 8761:8761 playgg/discovery-service:latest

docker run --name user-service --rm -p 8081:8081 ^
  -e EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://host.docker.internal:8761/eureka/ ^
  playgg/user-service:latest
```

## Docker Compose

Docker Compose permite levantar toda la infraestructura declarada en `docker-compose.yml` con un solo comando. En este proyecto se usa para iniciar MySQL, Eureka, Gateway y los nueve microservicios de dominio en una misma red Docker.

### Requisitos

- Docker Desktop instalado y en ejecucion.
- Docker Compose v2 disponible mediante el comando `docker compose`.
- Puertos libres en la maquina: `3306`, `8761` y `8080` a `8089`.
- Ejecutar los comandos desde la raiz del proyecto `PLAY.GG`.

No es necesario instalar Java ni Maven localmente para levantar el entorno con Compose. Cada imagen compila su servicio usando Maven dentro del Dockerfile y luego lo ejecuta con Java 17.

### Iniciar el proyecto completo

Desde la raiz del proyecto:

```bash
docker compose up
```

Para construir las imagenes desde cero y luego iniciar:

```bash
docker compose up --build
```

Cuando todos los contenedores esten arriba, los accesos principales son:

| Servicio | URL |
| --- | --- |
| Gateway | `http://localhost:8080` |
| Eureka | `http://localhost:8761` |
| MySQL | `localhost:3306` |
| user-service | `http://localhost:8081` |
| auth-service | `http://localhost:8082` |
| profile-service | `http://localhost:8083` |
| forum-service | `http://localhost:8084` |
| community-service | `http://localhost:8085` |
| chat-service | `http://localhost:8086` |
| notification-service | `http://localhost:8087` |
| game-service | `http://localhost:8088` |
| collectible-service | `http://localhost:8089` |

### Detener el proyecto

Detener contenedores conservando la base de datos:

```bash
docker compose down
```

Detener contenedores y eliminar tambien el volumen persistente de MySQL:

```bash
docker compose down -v
```

Ver logs de todos los servicios:

```bash
docker compose logs -f
```

Ver logs de un servicio especifico:

```bash
docker compose logs -f user-service
```

### Arquitectura Docker utilizada

El archivo `docker-compose.yml` crea:

- una red bridge llamada `playgg-network`, donde todos los contenedores se resuelven por nombre;
- un contenedor `mysql` con imagen `mysql:8.4`;
- un volumen persistente `mysql-data` montado en `/var/lib/mysql`;
- un contenedor `discovery-service` como servidor Eureka;
- un contenedor `gateway-service` como entrada unica HTTP;
- nueve microservicios de dominio conectados a Eureka y, cuando corresponde, a MySQL.

Los servicios Spring Boot reciben variables de entorno para reemplazar las URLs locales de desarrollo:

- `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://discovery-service:8761/eureka/`
- `SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/...`
- `SPRING_DATASOURCE_USERNAME=root`
- `SPRING_DATASOURCE_PASSWORD=`

La comunicacion con OpenFeign se mantiene por nombres de servicio (`user-service`, `game-service`, etc.). Gateway tambien usa rutas `lb://...`, por lo que Eureka resuelve las instancias registradas dentro de la red Docker.

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

## Autenticacion basica

`auth-service` permite registrar usuarios e iniciar sesion de forma simple. No guarda sesiones propias: consulta a `user-service` mediante Feign, valida credenciales y devuelve datos basicos del usuario.

Esta implementacion separa autenticacion y usuarios sin agregar OAuth ni manejo avanzado de sesiones.

## Bases de datos

Cada microservicio de dominio usa su propia base de datos MySQL. Con XAMPP se puede iniciar MySQL/MariaDB y crear las bases desde phpMyAdmin, o dejar que Spring las cree si el usuario tiene permisos.

```sql
CREATE DATABASE IF NOT EXISTS playgg_users_db;
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
3. Levantar primero Eureka:

```bash
mvn -pl discovery-service spring-boot:run
```

4. Levantar Gateway:

```bash
mvn -pl gateway-service spring-boot:run
```

5. Levantar los servicios de dominio que se quieran probar. Para una presentacion completa, este orden es el mas comodo:

```bash
mvn -pl user-service spring-boot:run
mvn -pl auth-service spring-boot:run
mvn -pl game-service spring-boot:run
mvn -pl profile-service spring-boot:run
mvn -pl forum-service spring-boot:run
mvn -pl community-service spring-boot:run
mvn -pl chat-service spring-boot:run
mvn -pl notification-service spring-boot:run
mvn -pl collectible-service spring-boot:run
```

6. Abrir Eureka en `http://localhost:8761` y confirmar que los servicios aparezcan registrados.

7. Probar la API desde el Gateway en `http://localhost:8080`. Por ejemplo, `POST http://localhost:8080/auth/register` o `GET http://localhost:8080/users`.

No es necesario compilar manualmente antes de levantar los servicios. Al ejecutar cada servicio con `spring-boot:run`, Maven compila lo necesario automaticamente.

## Pruebas Unitarias

Todos los modulos heredan `spring-boot-starter-test` desde el POM padre, por lo que el proyecto usa JUnit 5 y Mockito sin agregar dependencias innecesarias.

JUnit 5 se usa como framework de pruebas. Mockito se usa para crear mocks, que son objetos simulados que reemplazan dependencias reales durante el test. Por ejemplo, un `Service` puede probarse usando un `Repository` mockeado, sin conectarse a MySQL. En una arquitectura de microservicios esto es importante porque permite validar la logica de negocio sin levantar otros servicios ni hacer llamadas HTTP reales.

Las pruebas unitarias se encuentran en `src/test/java`, manteniendo el mismo package de `src/main/java`. No usan `@SpringBootTest`; trabajan con `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@InjectMocks`, `when(...)`, `verify(...)`, `assertEquals`, `assertThrows` y `assertNotNull`.

Clases con pruebas unitarias:

- `auth-service`: `AuthServiceTest`
- `user-service`: `UserServiceTest`
- `profile-service`: `ProfileServiceTest`
- `forum-service`: `PostServiceTest` y `CommentServiceTest`
- `community-service`: `CommunityServiceTest`
- `chat-service`: `MessageServiceTest`
- `notification-service`: `NotificationServiceTest`
- `game-service`: `GameServiceTest`
- `collectible-service`: `CollectibleServiceTest`

Las pruebas cubren casos principales de crear, buscar, actualizar, eliminar y manejo de errores donde corresponde. Tambien verifican que los clientes Feign se usen como mocks, evitando llamadas reales entre microservicios.

Lo que falta:

- `discovery-service` no tiene pruebas unitarias de Service porque funciona como servidor Eureka y no posee CRUD de negocio.
- `gateway-service` no tiene pruebas unitarias de Service porque su responsabilidad principal es enrutar peticiones con Spring Cloud Gateway.
- Si mas adelante se agregan reglas propias en esos modulos, se pueden crear pruebas unitarias para esas clases.

Para ejecutar todos los tests del proyecto:

```bash
mvn test
```

Tambien se puede ejecutar un modulo especifico:

```bash
mvn -pl user-service test
```

## Guia rapida para presentacion

Para mostrar el flujo de datos de forma clara, conviene ingresar datos por Postman o Insomnia usando siempre el Gateway (`localhost:8080`). El Gateway redirige al microservicio correcto y cada microservicio guarda sus propios datos en su base MySQL.

| Paso | Peticion | Servicio que responde | Donde se guarda |
| --- | --- | --- | --- |
| 1 | `POST /auth/register` | `auth-service` crea el usuario usando `user-service` por Feign | `playgg_users_db.users` |
| 2 | `POST /auth/login` | `auth-service` valida credenciales consultando `user-service` | No guarda datos propios |
| 3 | `POST /games` | `game-service` | `playgg_games_db.games` |
| 4 | `POST /profiles` | `profile-service` valida `userId` y `favoriteGameId` por Feign | `playgg_profiles_db.profiles` |
| 5 | `POST /posts` | `forum-service` valida `userId` por Feign | `playgg_forum_db.posts` |
| 6 | `POST /comments` | `forum-service` valida `postId` local y `userId` por Feign | `playgg_forum_db.comments` |
| 7 | `POST /communities` | `community-service` valida `ownerId` por Feign | `playgg_communities_db.communities` |
| 8 | `POST /messages` | `chat-service` valida emisor y receptor por Feign | `playgg_chat_db.messages` |
| 9 | `POST /notifications` | `notification-service` valida `userId` por Feign | `playgg_notifications_db.notifications` |
| 10 | `POST /collectibles` | `collectible-service` valida `userId` y `gameId` por Feign | `playgg_collectibles_db.collectibles` |

## Dos formas de probar la API

### Metodo 1: JSON manual por Gateway

Este es el metodo recomendado para probar el flujo completo de microservicios. Se usa Thunder Client, Postman o Insomnia apuntando al Gateway:

```text
http://localhost:8080
```

En cada peticion `POST` o `PUT` se selecciona `Body > JSON` y se pega el JSON correspondiente. Ejemplo:

```text
POST http://localhost:8080/auth/register
```

```json
{
  "nickname": "playerOne",
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan@mail.com",
  "password": "12345678",
  "country": "Chile"
}
```

### Metodo 2: Swagger UI por microservicio

Swagger permite ver y ejecutar los endpoints desde el navegador. En este proyecto se habilito Swagger solo en los microservicios REST de dominio:

| Servicio | Swagger UI | OpenAPI JSON |
| --- | --- | --- |
| `user-service` | `http://localhost:8081/swagger-ui.html` | `http://localhost:8081/v3/api-docs` |
| `auth-service` | `http://localhost:8082/swagger-ui.html` | `http://localhost:8082/v3/api-docs` |
| `profile-service` | `http://localhost:8083/swagger-ui.html` | `http://localhost:8083/v3/api-docs` |
| `forum-service` | `http://localhost:8084/swagger-ui.html` | `http://localhost:8084/v3/api-docs` |
| `community-service` | `http://localhost:8085/swagger-ui.html` | `http://localhost:8085/v3/api-docs` |
| `chat-service` | `http://localhost:8086/swagger-ui.html` | `http://localhost:8086/v3/api-docs` |
| `notification-service` | `http://localhost:8087/swagger-ui.html` | `http://localhost:8087/v3/api-docs` |
| `game-service` | `http://localhost:8088/swagger-ui.html` | `http://localhost:8088/v3/api-docs` |
| `collectible-service` | `http://localhost:8089/swagger-ui.html` | `http://localhost:8089/v3/api-docs` |

Para usar Swagger:

1. Levantar Eureka.
2. Levantar el microservicio que se quiere probar.
3. Abrir la URL `swagger-ui.html` del servicio.
4. Elegir un endpoint.
5. Presionar `Try it out`.
6. Completar el JSON si el endpoint recibe body.
7. Presionar `Execute`.

`gateway-service` y `discovery-service` no tienen Swagger propio. Gateway se usa para el metodo manual con JSON; Swagger se abre directo en el puerto de cada microservicio REST.

Ejemplo de registro:

```json
{
  "nickname": "playerOne",
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan@mail.com",
  "password": "12345678",
  "country": "Chile"
}
```

Ejemplo de juego:

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

Ejemplo de perfil, asumiendo `userId = 1` y `gameId = 1`:

```json
{
  "userId": 1,
  "avatarUrl": "https://example.com/avatar.png",
  "bannerUrl": "https://example.com/banner.png",
  "bio": "Jugador competitivo de FPS",
  "steamUsername": "playerOneSteam",
  "discordUsername": "playerOne#1234",
  "favoriteGameId": 1,
  "rank": "Gold",
  "level": 12
}
```

## Ejemplos JSON por microservicio

Los campos que terminan en `Id` deben existir antes de enviar la peticion. Por ejemplo, para crear un perfil primero debe existir el usuario (`userId`) y, si se informa juego favorito, tambien debe existir el juego (`favoriteGameId`).

### auth-service

Registro por `POST /auth/register`:

```json
{
  "nickname": "playerOne",
  "firstName": "Juan",
  "lastName": "Perez",
  "email": "juan@mail.com",
  "password": "12345678",
  "country": "Chile"
}
```

Login por `POST /auth/login`:

```json
{
  "email": "juan@mail.com",
  "password": "12345678"
}
```

### user-service

Crear usuario por `POST /users`:

```json
{
  "nickname": "playerTwo",
  "firstName": "Maria",
  "lastName": "Lopez",
  "email": "maria@mail.com",
  "password": "12345678",
  "country": "Chile",
  "role": "PLAYER",
  "active": true
}
```

Roles validos: `PLAYER`, `ADMIN`.

### game-service

Crear juego por `POST /games`:

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

### profile-service

Crear perfil por `POST /profiles`:

```json
{
  "userId": 1,
  "avatarUrl": "https://example.com/avatar.png",
  "bannerUrl": "https://example.com/banner.png",
  "bio": "Jugador competitivo de FPS",
  "steamUsername": "playerOneSteam",
  "discordUsername": "playerOne#1234",
  "favoriteGameId": 1,
  "rank": "Gold",
  "level": 12
}
```

`userId` debe existir en `user-service`. `favoriteGameId` es opcional, pero si se envia debe existir en `game-service`.

### forum-service

Crear post por `POST /posts`:

```json
{
  "userId": 1,
  "title": "Mejor agente para empezar",
  "content": "Estoy empezando en Valorant, que agente recomiendan?",
  "category": "Valorant"
}
```

Crear comentario por `POST /comments`:

```json
{
  "postId": 1,
  "userId": 1,
  "content": "Sage es buena opcion para aprender."
}
```

### community-service

Crear comunidad por `POST /communities`:

```json
{
  "ownerId": 1,
  "name": "Valorant Chile",
  "description": "Comunidad para buscar equipo y compartir partidas",
  "bannerUrl": "https://example.com/banner-valorant.jpg"
}
```

Agregar miembro por `POST /communities/{id}/members`:

```json
{
  "userId": 2,
  "role": "MEMBER"
}
```

Roles validos: `OWNER`, `MODERATOR`, `MEMBER`.

### chat-service

Crear mensaje por `POST /messages`:

```json
{
  "senderId": 1,
  "receiverId": 2,
  "content": "Hola, jugamos?",
  "read": false
}
```

`senderId` y `receiverId` deben existir en `user-service`.

### notification-service

Crear notificacion por `POST /notifications`:

```json
{
  "userId": 1,
  "title": "Nuevo comentario",
  "message": "Alguien comento tu publicacion",
  "type": "COMMENT",
  "read": false
}
```

Tipos validos: `MESSAGE`, `COMMENT`, `COMMUNITY_INVITE`, `FRIEND_REQUEST`.

### collectible-service

Crear coleccionable por `POST /collectibles`:

```json
{
  "userId": 1,
  "gameId": 1,
  "name": "Primera victoria",
  "description": "Ganaste tu primera partida competitiva",
  "rarity": "RARE"
}
```

Rarezas validas: `COMMON`, `RARE`, `EPIC`, `LEGENDARY`.

## Flujo de microservicios

```mermaid
flowchart LR
    Cliente[Cliente o Postman] --> Gateway[Gateway :8080]
    Gateway --> Eureka[Eureka :8761]
    Gateway --> User[user-service :8081]
    Gateway --> Auth[auth-service :8082]
    Gateway --> Profile[profile-service :8083]
    Gateway --> Forum[forum-service :8084]
    Gateway --> Community[community-service :8085]
    Gateway --> Chat[chat-service :8086]
    Gateway --> Notification[notification-service :8087]
    Gateway --> Game[game-service :8088]
    Gateway --> Collectible[collectible-service :8089]

    Auth -->|Feign| User
    Profile -->|Feign| User
    Profile -->|Feign| Game
    Forum -->|Feign| User
    Community -->|Feign| User
    Chat -->|Feign| User
    Notification -->|Feign| User
    Collectible -->|Feign| User
    Collectible -->|Feign| Game

    User --> DBUsers[(playgg_users_db)]
    Profile --> DBProfiles[(playgg_profiles_db)]
    Forum --> DBForum[(playgg_forum_db)]
    Community --> DBCommunity[(playgg_communities_db)]
    Chat --> DBChat[(playgg_chat_db)]
    Notification --> DBNotifications[(playgg_notifications_db)]
    Game --> DBGames[(playgg_games_db)]
    Collectible --> DBCollectibles[(playgg_collectibles_db)]
```

Flujo general de ingreso o consulta:

```text
Cliente -> Gateway -> Controller -> Service -> Repository -> MySQL
```

Si el dato depende de otro microservicio, el `Service` valida primero por Feign. Ejemplo: `profile-service` recibe `userId` y `favoriteGameId`, consulta `user-service` y `game-service`, y solo despues guarda el perfil en su propia base de datos.

## Endpoints principales

- `POST /users`
- `GET /users`
- `GET /users/{id}`
- `GET /users/nickname/{nickname}`
- `GET /users/email/{email}`
- `GET /users/internal/auth/email/{email}`
- `POST /auth/register`
- `POST /auth/login`
- `POST /profiles`
- `POST /posts`
- `POST /comments`
- `POST /communities`
- `POST /communities/{id}/members`
- `POST /messages`
- `POST /notifications`
- `POST /games`
- `POST /collectibles`

## Ideas clave

- El sistema se divide por dominios para que cada servicio tenga una responsabilidad clara.
- Eureka permite descubrir servicios por nombre.
- Gateway centraliza el acceso externo.
- Controller, Service y Repository separan entrada HTTP, reglas de negocio y persistencia.
- Los DTOs evitan exponer entidades JPA directamente.
- Feign se usa solo para validar datos externos necesarios.
- No hay relaciones JPA entre microservicios; solo ids como `userId`, `gameId` o `postId`.
- Las relaciones JPA locales se mantienen donde ayudan a representar el dominio.
- La autenticacion se concentra en `auth-service`, por lo que la validacion de credenciales no queda repartida por todo el proyecto.
