# Guia de ejecucion PLAY.GG

Esta guia explica como preparar, ejecutar y probar los microservicios del proyecto PLAY.GG paso a paso.

## 1. Requisitos

Instalar o tener disponible:

- Java 17
- Maven
- XAMPP
- Postman o Insomnia
- Navegador web

Si Maven no funciona con `mvn`, se puede usar el Maven incluido por el wrapper/local de tu equipo si ya esta configurado.

## 2. Iniciar MySQL con XAMPP

1. Abrir XAMPP.
2. Iniciar el servicio **MySQL**.
3. Entrar a phpMyAdmin:

```text
http://localhost/phpmyadmin
```

4. Crear las bases de datos:

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

`auth-service` no usa base de datos propia en la version actual.

## 3. Configurar conexion a MySQL

Cada microservicio que guarda datos tiene su archivo:

```text
src/main/resources/application.yml
```

Los archivos que debes revisar son:

```text
user-service/src/main/resources/application.yml
profile-service/src/main/resources/application.yml
forum-service/src/main/resources/application.yml
community-service/src/main/resources/application.yml
chat-service/src/main/resources/application.yml
notification-service/src/main/resources/application.yml
game-service/src/main/resources/application.yml
collectible-service/src/main/resources/application.yml
```

Ejemplo de configuracion:

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/playgg_users_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
    username: root
    password:
```

Si tu MySQL usa password, escribirla en:

```yml
password: tu_password
```

Si usas XAMPP por defecto, normalmente queda:

```yml
username: root
password:
```

## 4. Compilar el proyecto

Desde la carpeta raiz del proyecto:

```bash
mvn clean package -DskipTests
```

Si `mvn` no esta reconocido, ejecuta los servicios desde IntelliJ IDEA o configura Maven en el PATH.

## 5. Orden para levantar los microservicios

Es importante respetar el orden inicial.

### Paso 1: levantar Eureka

```bash
mvn -pl discovery-service spring-boot:run
```

Abrir en el navegador:

```text
http://localhost:8761
```

Eureka sirve para ver que los microservicios estan registrados.

### Paso 2: levantar Gateway

En otra terminal:

```bash
mvn -pl gateway-service spring-boot:run
```

Gateway queda disponible en:

```text
http://localhost:8080
```

Gateway es la entrada principal para probar la API.

### Paso 3: levantar servicios base

En terminales separadas:

```bash
mvn -pl user-service spring-boot:run
mvn -pl auth-service spring-boot:run
mvn -pl game-service spring-boot:run
```

Con esos tres ya puedes registrar usuarios, iniciar sesion y crear juegos.

### Paso 4: levantar los demas servicios

```bash
mvn -pl profile-service spring-boot:run
mvn -pl forum-service spring-boot:run
mvn -pl community-service spring-boot:run
mvn -pl chat-service spring-boot:run
mvn -pl notification-service spring-boot:run
mvn -pl collectible-service spring-boot:run
```

## 6. Puertos

| Servicio | Puerto | Uso |
| --- | ---: | --- |
| discovery-service | 8761 | Eureka |
| gateway-service | 8080 | Entrada principal |
| user-service | 8081 | Usuarios |
| auth-service | 8082 | Registro y login |
| profile-service | 8083 | Perfiles |
| forum-service | 8084 | Posts y comentarios |
| community-service | 8085 | Comunidades |
| chat-service | 8086 | Mensajes |
| notification-service | 8087 | Notificaciones |
| game-service | 8088 | Juegos |
| collectible-service | 8089 | Coleccionables |

## 7. Como usar Eureka

Eureka se abre en:

```text
http://localhost:8761
```

Sirve para comprobar que los servicios estan activos.

Cuando levantes un servicio, deberia aparecer con su nombre, por ejemplo:

```text
USER-SERVICE
AUTH-SERVICE
GAME-SERVICE
PROFILE-SERVICE
```

Si un servicio no aparece, revisa:

- Que el servicio este corriendo.
- Que no haya error en consola.
- Que `discovery-service` este levantado.
- Que el `application.yml` tenga Eureka apuntando a `http://localhost:8761/eureka/`.

## 8. Como usar Gateway

En vez de llamar cada microservicio por su puerto interno, usa:

```text
http://localhost:8080
```

Ejemplos:

```text
POST http://localhost:8080/auth/register
POST http://localhost:8080/auth/login
GET  http://localhost:8080/users
POST http://localhost:8080/games
POST http://localhost:8080/profiles
```

Gateway redirige automaticamente al servicio correspondiente.

## 9. Flujo recomendado para probar en Postman

### 1. Registrar usuario

```http
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

Respuesta esperada:

```json
{
  "userId": 1,
  "nickname": "playerOne",
  "email": "juan@mail.com",
  "role": "PLAYER",
  "message": "Usuario registrado correctamente"
}
```

El usuario se guarda en:

```text
playgg_users_db.users
```

### 2. Iniciar sesion

```http
POST http://localhost:8080/auth/login
```

```json
{
  "email": "juan@mail.com",
  "password": "12345678"
}
```

Respuesta esperada:

```json
{
  "userId": 1,
  "nickname": "playerOne",
  "email": "juan@mail.com",
  "role": "PLAYER",
  "message": "Login correcto"
}
```

### 3. Crear juego

```http
POST http://localhost:8080/games
```

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

El juego se guarda en:

```text
playgg_games_db.games
```

### 4. Crear perfil

Usa `userId` del usuario creado y `favoriteGameId` del juego creado.

```http
POST http://localhost:8080/profiles
```

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

Si el usuario no tiene juego favorito, puedes enviar:

```json
{
  "userId": 1,
  "bio": "Jugador nuevo en PLAY.GG"
}
```

En ese caso `favoriteGameId` queda como `null`. En un frontend se puede mostrar como:

```text
Sin juego favorito
```

### 5. Crear post

```http
POST http://localhost:8080/posts
```

```json
{
  "userId": 1,
  "title": "Mejor agente para empezar",
  "content": "Estoy empezando en Valorant, que agente recomiendan?",
  "category": "Valorant"
}
```

### 6. Crear comentario

```http
POST http://localhost:8080/comments
```

```json
{
  "postId": 1,
  "userId": 1,
  "content": "Sage es buena opcion para aprender."
}
```

### 7. Crear comunidad

```http
POST http://localhost:8080/communities
```

```json
{
  "ownerId": 1,
  "name": "Valorant Chile",
  "description": "Comunidad para buscar equipo y compartir partidas",
  "bannerUrl": "https://example.com/banner-valorant.jpg"
}
```

Al crear la comunidad, el duenio tambien queda agregado como miembro `OWNER`.

### 8. Agregar miembro a comunidad

Primero debe existir otro usuario, por ejemplo `userId = 2`.

```http
POST http://localhost:8080/communities/1/members
```

```json
{
  "userId": 2,
  "role": "MEMBER"
}
```

Roles validos:

```text
OWNER
MODERATOR
MEMBER
```

### 9. Enviar mensaje

Primero deben existir dos usuarios.

```http
POST http://localhost:8080/messages
```

```json
{
  "senderId": 1,
  "receiverId": 2,
  "content": "Hola, jugamos?",
  "read": false
}
```

### 10. Crear notificacion

```http
POST http://localhost:8080/notifications
```

```json
{
  "userId": 1,
  "title": "Nuevo comentario",
  "message": "Alguien comento tu publicacion",
  "type": "COMMENT",
  "read": false
}
```

Tipos validos:

```text
MESSAGE
COMMENT
COMMUNITY_INVITE
FRIEND_REQUEST
```

### 11. Crear coleccionable

Debe existir `userId` y `gameId`.

```http
POST http://localhost:8080/collectibles
```

```json
{
  "userId": 1,
  "gameId": 1,
  "name": "Primera victoria",
  "description": "Ganaste tu primera partida competitiva",
  "rarity": "RARE"
}
```

Rarezas validas:

```text
COMMON
RARE
EPIC
LEGENDARY
```

## 10. Consultas utiles

Listar usuarios:

```http
GET http://localhost:8080/users
```

Buscar usuario por id:

```http
GET http://localhost:8080/users/1
```

Listar juegos:

```http
GET http://localhost:8080/games
```

Listar perfiles:

```http
GET http://localhost:8080/profiles
```

Listar posts:

```http
GET http://localhost:8080/posts
```

Listar comentarios de un post:

```http
GET http://localhost:8080/comments/post/1
```

Listar miembros de una comunidad:

```http
GET http://localhost:8080/communities/1/members
```

## 11. Flujo interno explicado

Cuando llamas un endpoint por Gateway, ocurre esto:

```text
Postman -> Gateway -> Microservicio -> Controller -> Service -> Repository -> MySQL
```

Si el microservicio necesita validar un dato externo, usa Feign:

```text
profile-service -> user-service
profile-service -> game-service
collectible-service -> user-service
collectible-service -> game-service
```

Ejemplo:

1. Enviar `POST /profiles` a Gateway.
2. Gateway redirige a `profile-service`.
3. `profile-service` valida `userId` en `user-service`.
4. Si viene `favoriteGameId`, valida el juego en `game-service`.
5. Si todo existe, guarda el perfil en `playgg_profiles_db.profiles`.

## 12. Problemas comunes

### El servicio no aparece en Eureka

Revisar:

- Que `discovery-service` este corriendo.
- Que el microservicio no tenga errores en consola.
- Que el puerto no este ocupado.

### Error de base de datos

Revisar:

- Que MySQL este iniciado en XAMPP.
- Que la base de datos exista.
- Que `username` y `password` sean correctos en `application.yml`.

### Gateway devuelve error

Revisar:

- Que Gateway este levantado.
- Que Eureka este levantado.
- Que el servicio destino aparezca registrado en Eureka.

### Error al crear perfil, post, mensaje o coleccionable

Probablemente falta crear primero el usuario o el juego.

Regla practica:

- Si usas `userId`, ese usuario debe existir.
- Si usas `gameId`, ese juego debe existir.
- Si usas `postId`, ese post debe existir.
- Si usas `communityId`, esa comunidad debe existir.

