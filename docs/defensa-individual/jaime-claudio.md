# Defensa Técnica Individual

## Nombre

**Claudio Jaime Calderón**

**Asignatura: Desarrollo Full Stack**

**Profesor: Carlos Martinez Sanchez**

**Sección: 011v**

---

# Rol dentro del equipo

Durante el desarrollo del proyecto participé principalmente en el módulo de gestión de usuarios. Mi trabajo estuvo enfocado en los microservicios **user-service**, **auth-service** y **profile-service**.

En esta parte del proyecto trabajé en las funcionalidades relacionadas con el registro de usuarios, el inicio de sesión, la administración de usuarios mediante un CRUD y la gestión de perfiles. Además, colaboré en la revisión y ejecución de las pruebas unitarias de estos servicios para verificar que las funcionalidades implementadas funcionaran correctamente.

---

# Funcionalidades o módulos en los que participé

## User Service

El **user-service** es el encargado de administrar los usuarios del sistema. En este microservicio trabajé con las operaciones principales de un CRUD, permitiendo crear, consultar, actualizar y eliminar usuarios.

La estructura utilizada sigue el modelo de Spring Boot, separando Controller, Service y Repository, lo que ayuda a mantener el código organizado. Además, se utilizaron DTO para enviar únicamente la información necesaria al cliente sin exponer directamente las entidades de la base de datos.

---

## Auth Service

El **auth-service** se encarga de la autenticación de los usuarios.

Aquí se encuentran las funciones de registro e inicio de sesión. Cuando un usuario quiere acceder a la plataforma, este servicio valida sus credenciales antes de permitir el acceso.

Separar la autenticación del manejo de usuarios ayuda a mantener cada servicio con una responsabilidad específica.

---

## Profile Service

El **profile-service** administra la información del perfil de cada usuario.

Mientras que el user-service almacena la información principal del usuario, este servicio guarda los datos relacionados con su perfil dentro de la plataforma, permitiendo consultarlos o modificarlos cuando sea necesario.

---

# Endpoints o flujos que domino

Los principales endpoints con los que trabajé fueron los relacionados con usuarios, autenticación y perfiles.

Entre ellos se encuentran:

* Registro de usuarios.
* Inicio de sesión.
* Crear usuario.
* Obtener usuarios.
* Buscar usuario por ID.
* Actualizar usuario.
* Eliminar usuario.
* Consultar perfil.
* Actualizar perfil.

El flujo general consiste en que el cliente envía una solicitud al API Gateway, este la redirige al microservicio correspondiente y finalmente se ejecuta la lógica necesaria para responder la petición.

---

# Pruebas asociadas

Dentro de estos microservicios se realizaron pruebas unitarias para comprobar que las operaciones principales funcionaran correctamente.

Las pruebas permiten verificar que la lógica implementada entregue los resultados esperados y ayudan a detectar errores antes de ejecutar la aplicación completa.

---

# Regla de negocio

Una de las reglas de negocio que mejor manejo es que un usuario debe estar registrado antes de poder iniciar sesión o administrar su información.

Además, cada usuario tiene asociado un único perfil, por lo que primero debe existir el usuario para posteriormente crear o modificar dicho perfil.

Cuando se intenta consultar, actualizar o eliminar un usuario que no existe, el sistema responde mediante excepciones controladas para informar correctamente el error.

---

# Relación de base de datos que conozco

La relación que mejor conozco es la existente entre Usuario y Perfil.

Cada usuario tiene asociado un único perfil y cada perfil pertenece únicamente a un usuario.

Esta separación permite mantener organizada la información y facilita el mantenimiento del sistema.

---

# Comunicación entre servicios que conozco

El proyecto utiliza una arquitectura basada en microservicios.

Cuando un usuario realiza una solicitud, esta primero pasa por el API Gateway.

Luego el Gateway envía la petición al microservicio correspondiente.

En el módulo en el que participé, el flujo principal es:

**Cliente → Gateway → Auth Service → User Service → Profile Service**

Además, todos los microservicios se registran en Eureka Server para que puedan encontrarse entre ellos de forma automática.

---

# Dificultad técnica

Una de las mayores dificultades fue entender cómo se relacionaban los distintos microservicios.

Al principio era fácil confundir las responsabilidades entre **user-service**, **auth-service** y **profile-service**. A medida que avanzó el proyecto fui entendiendo mejor la responsabilidad de cada microservicio y cómo se comunicaban entre sí. Eso me ayudó a comprender por qué es importante separar las funciones en una arquitectura de este tipo.

También tuve que familiarizarme con la estructura de Spring Boot y entender el recorrido que sigue una petición desde el Controller hasta el Repository, además del uso de DTO para separar las entidades de las respuestas entregadas por la API.

---

# Conocimiento general del proyecto

Aunque mi trabajo estuvo enfocado principalmente en el módulo de usuarios, también conozco la estructura general del proyecto.

Play.gg está compuesto por varios microservicios, entre ellos:

* Gateway Service
* Discovery Service (Eureka)
* Auth Service
* User Service
* Profile Service
* Forum Service
* Community Service
* Chat Service
* Notification Service
* Game Service
* Collectible Service

Cada uno tiene una responsabilidad específica y juntos permiten que la plataforma funcione de manera organizada.

Aunque mi trabajo estuvo centrado en el módulo de usuarios, durante el desarrollo también pude entender cómo interactúan los demás servicios dentro de la arquitectura general del proyecto.

---

# Aprendizaje personal

Este proyecto ha sido una de las experiencias más completas que he tenido durante la carrera hasta ahora.

Gracias a su desarrollo pude comprender mejor cómo funciona una arquitectura basada en microservicios y la importancia de separar las responsabilidades entre distintos servicios. También reforcé conocimientos sobre el desarrollo de APIs REST con Spring Boot, el uso de DTO, JPA, el manejo de excepciones y la realización de pruebas unitarias.

Además de aprender nuevos conceptos técnicos, este proyecto me permitió entender mejor cómo se desarrolla una aplicación en equipo, cómo organizar el código y la importancia de que cada integrante conozca bien el módulo en el que trabaja. Considero que fue una experiencia que me ayudó a fortalecer tanto mis conocimientos técnicos como la forma de enfrentar proyectos más grandes.
