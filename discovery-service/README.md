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

## Pruebas con Thunder Client

Este servicio no recibe JSON ni expone CRUD de negocio. Para comprobar que funciona, abrir:

```text
GET http://localhost:8761
```

Normalmente se revisa desde el navegador, porque Eureka muestra una consola HTML con los servicios registrados.

Orden recomendado:

1. Levantar `discovery-service`.
2. Levantar `gateway-service`.
3. Levantar los microservicios.
4. Verificar en `http://localhost:8761` que aparezcan registrados.

## Pruebas Unitarias

Este modulo hereda JUnit 5 y Mockito desde el POM padre, igual que el resto del proyecto.

Actualmente `discovery-service` no tiene una clase `Service` de negocio ni CRUD propio para probar de forma unitaria. Su responsabilidad principal es levantar Eureka Server mediante configuracion de Spring Cloud.

Lo que falta:

- No hay pruebas unitarias especificas para este modulo porque no existe logica de negocio aislable.
- Si mas adelante se agrega una clase propia con reglas o validaciones, se debe crear su test en `src/test/java/com/playgg/discovery`.

Mockito se usa en los microservicios de negocio para crear mocks, que son objetos simulados que reemplazan dependencias reales durante el test.

Para ejecutar todos los tests del proyecto:

```bash
mvn test
```
