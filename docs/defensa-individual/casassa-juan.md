# Defensa Técnica Individual – PLAY.GG

## Nombre

**Juan Pablo Casassa**

**Asignatura:** Desarrollo Full Stack I  
**Proyecto:** PLAY.GG  

---

# Rol dentro del equipo

Durante el desarrollo de PLAY.GG participé principalmente en los microservicios `game-service` y `collectible-service`, además de trabajar en la integración general del proyecto mediante `gateway-service` y `discovery-service`.

También trabajé con tecnologías utilizadas de forma transversal en el proyecto, como Eureka, OpenFeign, Swagger, pruebas con JUnit y Mockito y, durante la etapa final, Docker y Docker Compose.

Aunque mi trabajo estuvo enfocado principalmente en estos componentes, también conozco el funcionamiento de los demás microservicios y cómo se relacionan dentro de la arquitectura.

---

# Microservicios principales

## Game Service

El `game-service` administra la información relacionada con los juegos disponibles en PLAY.GG y mantiene su propia persistencia mediante JPA y MySQL.

Otros microservicios pueden consultar información de juegos mediante su API sin acceder directamente a su base de datos.

## Collectible Service

El `collectible-service` administra los coleccionables y se comunica con otros servicios cuando necesita información externa.

Por ejemplo:

**Collectible → User Service**

**Collectible → Game Service**

Esta comunicación se realiza mediante OpenFeign, manteniendo separadas las responsabilidades y bases de datos de cada microservicio.

---

# Gateway y Eureka

El `gateway-service` funciona como punto de entrada de PLAY.GG y redirige las solicitudes hacia el microservicio correspondiente.

El `discovery-service` utiliza Eureka Server para registrar los microservicios disponibles.

Gracias a esta integración, el Gateway puede localizar servicios utilizando nombres como:

`lb://user-service`

en lugar de depender directamente de una dirección IP o puerto fijo.

La implementación funcional de Gateway junto con Eureka fue uno de los principales puntos técnicos destacados durante la evaluación del proyecto.

---

# Conocimiento general del proyecto

Aunque mi trabajo estuvo enfocado principalmente en Game, Collectible y la integración general, también conozco la función de los demás servicios:

- `user-service`: gestión de usuarios.
- `auth-service`: registro y autenticación.
- `profile-service`: perfiles.
- `forum-service`: publicaciones y comentarios.
- `community-service`: comunidades.
- `chat-service`: mensajes.
- `notification-service`: notificaciones.

También comprendo cómo OpenFeign permite la comunicación entre estos servicios sin que un microservicio tenga que acceder directamente a la base de datos de otro.

---

# Pruebas y documentación

El proyecto utiliza JUnit y Mockito para realizar pruebas unitarias.

Mockito permite simular dependencias como Repository o clientes OpenFeign, haciendo posible probar un microservicio sin necesitar una base de datos real o levantar todos los demás servicios.

Según el feedback recibido, PLAY.GG contó con aproximadamente 60 tests reales distribuidos en 9 de los 11 servicios.

También se incorporó Swagger/OpenAPI para documentar los endpoints y facilitar la comprensión y prueba de las APIs REST.

---

# Docker y Docker Compose

Como parte final del proyecto se realizó la containerización mediante Docker.

Los Dockerfile definen cómo se construyen y ejecutan los servicios dentro de contenedores.

También se configuró Docker Compose para coordinar la ejecución de los diferentes componentes de PLAY.GG, incluyendo los microservicios y la infraestructura necesaria.

Esto permite facilitar la ejecución conjunta del proyecto sin tener que iniciar manualmente cada microservicio desde el IDE.

---

# Evidencia de participación

Mi participación se encuentra principalmente relacionada con:

- `game-service`
- `collectible-service`
- `gateway-service`
- `discovery-service`
- Integraciones mediante OpenFeign.
- Eureka y Gateway.
- Pruebas con JUnit y Mockito.
- Swagger/OpenAPI.
- Docker y Docker Compose.


# Aprendizaje personal

PLAY.GG me permitió entender de manera práctica cómo funciona una arquitectura de microservicios y, principalmente, cómo lograr que diferentes servicios trabajen juntos.

Durante el proyecto pude comprender el papel de Gateway como punto de entrada, Eureka para encontrar los servicios y OpenFeign para la comunicación entre ellos.

También pude trabajar con pruebas mediante JUnit y Mockito, documentación mediante Swagger y finalmente Docker y Docker Compose para containerizar y facilitar la ejecución conjunta del proyecto.

Uno de mis principales aprendizajes fue entender que los microservicios no consisten solamente en separar una aplicación en varios proyectos, sino en definir correctamente la responsabilidad de cada servicio y permitir que puedan comunicarse manteniendo su independencia.