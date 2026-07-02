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

## Resumen

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

## Pruebas con Thunder Client

Gateway no recibe un JSON propio. Se usa para llamar a los endpoints de los demas servicios desde una sola base URL:

```text
http://localhost:8080
```

Ejemplos:

- `POST http://localhost:8080/auth/register`
- `POST http://localhost:8080/auth/login`
- `GET http://localhost:8080/users`
- `POST http://localhost:8080/games`
- `POST http://localhost:8080/profiles`
- `POST http://localhost:8080/posts`
- `POST http://localhost:8080/communities`
- `POST http://localhost:8080/messages`
- `POST http://localhost:8080/notifications`
- `POST http://localhost:8080/collectibles`

Para probar por Gateway deben estar levantados `discovery-service`, `gateway-service` y el servicio destino.

## Swagger

`gateway-service` no tiene Swagger propio porque no expone CRUD de negocio. Para probar con JSON manual se usa Gateway en `http://localhost:8080`. Para probar con Swagger se debe abrir la URL del microservicio REST directo, por ejemplo:

```text
http://localhost:8081/swagger-ui.html
```

Cada README de servicio indica su URL de Swagger correspondiente.

## Pruebas Unitarias

Este modulo hereda JUnit 5 y Mockito desde el POM padre, igual que el resto del proyecto.

Actualmente `gateway-service` no tiene una clase `Service` de negocio ni CRUD propio para probar de forma unitaria. Su responsabilidad principal esta en la configuracion de rutas con Spring Cloud Gateway.

Lo que falta:

- No hay pruebas unitarias especificas para este modulo porque no existe logica de negocio aislable.
- Si mas adelante se agregan filtros, validaciones o reglas propias, se deben crear tests unitarios en `src/test/java/com/playgg/gateway`.

Mockito se usa en los microservicios de negocio para crear mocks, que son objetos simulados que reemplazan dependencias reales durante el test.

Para ejecutar todos los tests del proyecto:

```bash
mvn test
```
