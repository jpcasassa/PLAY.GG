# Diagrama de arquitectura de microservicios PLAY.GG

Este diagrama muestra la arquitectura general del proyecto: cliente, API Gateway, Eureka, microservicios de dominio, comunicacion interna con OpenFeign y bases de datos MySQL por servicio.

```mermaid
flowchart TB
  client["Cliente API<br/>Postman / Insomnia / Navegador"]

  subgraph edge["Entrada al sistema"]
    gateway["gateway-service<br/>Spring Cloud Gateway<br/>Puerto 8080"]
  end

  subgraph discovery["Descubrimiento de servicios"]
    eureka["discovery-service<br/>Eureka Server<br/>Puerto 8761"]
  end

  subgraph services["Microservicios PLAY.GG"]
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

  gateway -->|"/auth/**"| auth
  gateway -->|"/users/**"| users
  gateway -->|"/profiles/**"| profiles
  gateway -->|"/posts/** /comments/**"| forum
  gateway -->|"/communities/**"| communities
  gateway -->|"/messages/**"| chat
  gateway -->|"/notifications/**"| notifications
  gateway -->|"/games/**"| games
  gateway -->|"/collectibles/**"| collectibles

  gateway -.->|"consulta registro"| eureka
  auth -.->|"registro Eureka"| eureka
  users -.->|"registro Eureka"| eureka
  profiles -.->|"registro Eureka"| eureka
  forum -.->|"registro Eureka"| eureka
  communities -.->|"registro Eureka"| eureka
  chat -.->|"registro Eureka"| eureka
  notifications -.->|"registro Eureka"| eureka
  games -.->|"registro Eureka"| eureka
  collectibles -.->|"registro Eureka"| eureka

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
  classDef gateway fill:#dbeafe,stroke:#1d4ed8,color:#0f172a
  classDef discovery fill:#fef3c7,stroke:#b45309,color:#0f172a
  classDef service fill:#dcfce7,stroke:#15803d,color:#0f172a
  classDef database fill:#fee2e2,stroke:#b91c1c,color:#0f172a

  class client client
  class gateway gateway
  class eureka discovery
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
