# gateway-service

## Para que sirve

Es el punto de entrada principal de la API. Recibe peticiones externas y las redirige al microservicio correspondiente usando Eureka.

## Utilidad en el proyecto

Permite que el cliente no tenga que conocer todos los puertos internos. Por ejemplo, puede entrar por `localhost:8080/users` y el Gateway envia la solicitud a `user-service`.

## Rutas configuradas

- `/users/**` hacia `user-service`
- `/auth/**` hacia `auth-service`
- `/profiles/**` hacia `profile-service`
- `/posts/**` y `/comments/**` hacia `forum-service`
- `/communities/**` hacia `community-service`
- `/messages/**` hacia `chat-service`
- `/notifications/**` hacia `notification-service`
- `/games/**` hacia `game-service`
- `/collectibles/**` hacia `collectible-service`

## Puerto

`8080`

## Como se usa

Primero debe estar levantado `discovery-service`:

```bash
mvn -pl gateway-service spring-boot:run
```

## Para la defensa

El Gateway centraliza el acceso a la plataforma y ordena las rutas. Es una pieza comun en microservicios porque reduce el acoplamiento entre frontend/cliente y servicios internos.

## Guia para leer el codigo

Este servicio tampoco tiene CRUD ni base de datos. Su tarea es recibir peticiones y redirigirlas.

Archivos importantes:

- `src/main/java/com/playgg/gateway/GatewayServiceApplication.java`: clase principal de Spring Boot.
- `src/main/resources/application.yml`: contiene las rutas del Gateway.
- `pom.xml`: contiene Spring Cloud Gateway y Eureka Client.

Que editar segun el cambio:

- Agregar una nueva ruta: editar `spring.cloud.gateway.routes` en `application.yml`.
- Cambiar el puerto del Gateway: editar `server.port`.
- Cambiar el servicio destino: editar `uri: lb://nombre-del-servicio`.

Ejemplo de ruta:

`Path=/users/**` envia las peticiones a `lb://user-service`. El prefijo `lb` indica balanceo/descubrimiento por Eureka.
