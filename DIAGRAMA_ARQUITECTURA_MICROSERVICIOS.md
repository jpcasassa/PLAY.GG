# Diagrama de arquitectura de microservicios PLAY.GG

Este diagrama muestra la arquitectura general del proyecto: cliente, API Gateway, Eureka, microservicios de dominio, comunicacion interna con OpenFeign y bases de datos MySQL por servicio.

```mermaid
%%{init: {"flowchart": {"nodeSpacing": 70, "rankSpacing": 95}} }%%
flowchart TB
  client["Cliente API<br/>Postman / Insomnia / Navegador"]
  gateway["gateway-service<br/>Spring Cloud Gateway<br/>Puerto 8080"]
  eureka["discovery-service<br/>Eureka Server<br/>Puerto 8761"]

  subgraph routes["Rutas expuestas por Gateway"]
    direction TB
    authRoute["/auth/**"]
    usersRoute["/users/**"]
    profilesRoute["/profiles/**"]
    forumRoute["/posts/**<br/>/comments/**"]
    communitiesRoute["/communities/**"]
    chatRoute["/messages/**"]
    notificationsRoute["/notifications/**"]
    gamesRoute["/games/**"]
    collectiblesRoute["/collectibles/**"]
  end

  subgraph services["Microservicios PLAY.GG"]
    direction TB
    auth["auth-service<br/>Registro y login<br/>Puerto 8082"]
    users["user-service<br/>Usuarios<br/>Puerto 8081"]
    profiles["profile-service<br/>Perfiles<br/>Puerto 8083"]
    forum["forum-service<br/>Posts y comentarios<br/>Puerto 8084"]
    communities["community-service<br/>Comunidades<br/>Puerto 8085"]
    chat["chat-service<br/>Mensajes<br/>Puerto 8086"]
    notifications["notification-service<br/>Notificaciones<br/>Puerto 8087"]
    games["game-service<br/>Juegos<br/>Puerto 8088"]
    collectibles["collectible-service<br/>Coleccionables<br/>Puerto 8089"]
  end

  subgraph mysql["MySQL / XAMPP"]
    direction TB
    usersDb[("playgg_users_db")]
    profilesDb[("playgg_profiles_db")]
    forumDb[("playgg_forum_db")]
    communitiesDb[("playgg_communities_db")]
    chatDb[("playgg_chat_db")]
    notificationsDb[("playgg_notifications_db")]
    gamesDb[("playgg_games_db")]
    collectiblesDb[("playgg_collectibles_db")]
  end

  client -->|"HTTP"| gateway
  gateway --> routes

  authRoute --> auth
  usersRoute --> users
  profilesRoute --> profiles
  forumRoute --> forum
  communitiesRoute --> communities
  chatRoute --> chat
  notificationsRoute --> notifications
  gamesRoute --> games
  collectiblesRoute --> collectibles

  gateway -.->|"consulta registro"| eureka
  services -.->|"registro Eureka"| eureka

  auth -.->|"OpenFeign"| users
  profiles -.->|"OpenFeign"| users
  profiles -.->|"OpenFeign"| games
  forum -.->|"OpenFeign"| users
  communities -.->|"OpenFeign"| users
  chat -.->|"OpenFeign"| users
  notifications -.->|"OpenFeign"| users
  collectibles -.->|"OpenFeign"| users
  collectibles -.->|"OpenFeign"| games

  users --> usersDb
  profiles --> profilesDb
  forum --> forumDb
  communities --> communitiesDb
  chat --> chatDb
  notifications --> notificationsDb
  games --> gamesDb
  collectibles --> collectiblesDb

  classDef client fill:#f8fafc,stroke:#334155,color:#0f172a
  classDef route fill:#f1f5f9,stroke:#64748b,color:#0f172a
  classDef gateway fill:#dbeafe,stroke:#1d4ed8,color:#0f172a
  classDef discovery fill:#fef3c7,stroke:#b45309,color:#0f172a
  classDef service fill:#dcfce7,stroke:#15803d,color:#0f172a
  classDef database fill:#fee2e2,stroke:#b91c1c,color:#0f172a

  class client client
  class gateway gateway
  class eureka discovery
  class authRoute,usersRoute,profilesRoute,forumRoute,communitiesRoute,chatRoute,notificationsRoute,gamesRoute,collectiblesRoute route
  class auth,users,profiles,forum,communities,chat,notifications,games,collectibles service
  class usersDb,profilesDb,forumDb,communitiesDb,chatDb,notificationsDb,gamesDb,collectiblesDb database
```

## Lectura rapida

- El cliente consume la API por `gateway-service` en `http://localhost:8080`.
- `gateway-service` enruta por path hacia cada microservicio usando los nombres registrados en Eureka.
- `discovery-service` mantiene el registro de servicios disponibles.
- Cada microservicio de dominio administra su propia base de datos MySQL.
- Las validaciones entre servicios se hacen con OpenFeign, principalmente hacia `user-service` y `game-service`.
- `auth-service` no tiene base de datos propia en esta version; usa `user-service` para registrar y validar usuarios.
