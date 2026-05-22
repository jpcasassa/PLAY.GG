# discovery-service

## Para que sirve

Este microservicio es el servidor Eureka de PLAY.GG. Su responsabilidad es registrar los microservicios activos para que puedan encontrarse entre ellos usando un nombre, por ejemplo `user-service`, en vez de depender de una URL fija.

## Utilidad en el proyecto

En una arquitectura de microservicios, cada servicio puede estar en un puerto distinto. Eureka ayuda a que el Gateway y los servicios con Feign sepan donde esta cada componente.

## Datos que maneja

No maneja entidades ni base de datos. Solo mantiene un registro temporal de servicios conectados.

## Puerto

`8761`

## Como se usa

Se debe levantar primero:

```bash
mvn -pl discovery-service spring-boot:run
```

Luego se levantan los demas servicios. Al entrar a `http://localhost:8761` se puede ver la consola de Eureka.

## Resumen

Eureka permite que los servicios se encuentren por nombre. Asi no hay que escribir IPs manualmente.

## Guia para leer el codigo

Este servicio es mas pequeno que los demas porque no tiene CRUD ni base de datos.

Archivos importantes:

- `src/main/java/com/playgg/discovery/DiscoveryServiceApplication.java`: clase principal. Contiene `@EnableEurekaServer`, que convierte este servicio en servidor Eureka.
- `src/main/resources/application.yml`: define el puerto `8761` y evita que Eureka se registre a si mismo como cliente.
- `pom.xml`: contiene la dependencia `spring-cloud-starter-netflix-eureka-server`.

Que editar segun el cambio:

- Cambiar puerto: editar `server.port` en `application.yml`.
- Cambiar nombre del servicio: editar `spring.application.name`.
- Revisar registro de servicios: abrir `http://localhost:8761` cuando el servicio este corriendo.

Este servicio no guarda datos; solo permite que los demas microservicios se encuentren por nombre.
