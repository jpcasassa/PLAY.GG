
# Construcción de los microservicios para la comunidad

---

**Nombre:** Martín Ignacio González Varela

**Asignatura:** Desarrollo Full Stack 1

**Profesor:** Carlos Martinez Sanchez

**Sección:** 011v

---

## Introducción:

El desarrollo de la plataforma que estamos desarrollando con mis compañeros es moderna y orientada a la comunidad ‘Gamer’.  
Para que el sistema tenga éxito tenemos que considerar 2 factores clave:

* **La retención :** ¿Qué significa?, bueno es la capacidad de lograr que los usuarios les llame la atención la plataforma y la vuelvan a ocupar en su dia a dia y no la abandonen.

* **Herramientas de interacción social :** ¿Qué significa?, bueno el ecosistema de ‘PLAY GG’ es lograr integrar que las comunidades de personas interactúen con la a nivel mundial, y para lograrlo necesitamos hacer funcionar los:

    * Espacios de discusión = Foros

    * Grupos de Interés = Clanes o Comunidades.

    * Comunicación Directa = Mensajería a través de un chat directo con personas.

    * Sistema de alertas = Notificaciones de tu creador o de tu comunidad de tu juego favorito.

Ambos factores cumplen un rol de manera importante con las herramientas interactivas que PLAY GG les proporciona a sus usuarios en su día a día.

---

## Los microservicios y sus responsabilidades:

| MICROSERVICIO        | PUERTO | BASE DE DATOS           | ENTIDADES |
| :---                 | :---:  | :---                    | :---      |
| forum-service        | 8084   | playgg_forum_db         | Publicaciones y Comentarios |
| community-service    | 8085   | playgg_commuties_db     | Comunidades y Miembros |
| chat-service         | 8086   | playgg_chat_db          | Mensajes privados y comversaciones |
| notification-service | 8087   | playgg_notifications_db | Notificaciones y Alertas de actividades |

### ¿Para qué sirve cada microservicio?

1. **Forum-service :** Es el espacio que se encarga de manejar y guardar las publicaciones y los comentarios de los usuarios.

2. **Community-service :** Es el espacio que se encarga de guardar la información de los clanes y la lista de jugadores que pertenecen a ellos.

3. **Chat-service :** Su entidad más importante son los mensajes que mandan los jugadores a otros.

4. **Notificación-service :** Se encarga de guardar las notificaciones clásicas, como por ejemplo ‘Tienes un mensaje pendiente’ o ‘A Juanito le ha gustado tu publicación’.

---

## 3) Creación:

Para la creación de los microservicios use Controller, Service - Repository.

### ¿Cómo funciona?

* **Controller :** Bueno el controller es la primera línea de defensa de nuestro servidor. Este es el único archivo que tiene contacto externo y directo con el internet.

* **Service :** Bueno, el service es nuestro cerebro del microservicio. Es el archivo donde se ejecuta toda la lógica de negocio, donde se aplican las reglas reales de ‘PLAY GG’ y se conectan con los otros microservicios a través de OpenFeign (es la herramienta que nos permite comunicar microservicios de forma limpia y rápida).

* **Repository :** Bueno, el repository es el archivo que se encarga exclusivamente de la base de datos MySQL. Es el único que tiene el acceso directo para escribir, leer, actualizar o borrar información en las tablas.

---

## 4) Construcción de la Red de Microservicios:

Para que todos los microservicios de PLAY GG funcionen con sincronización y de forma transparente para los usuarios se necesitan dos componentes muy importantes.

### ¿Cuáles son?

#### 1) Discovery-service (Eureka Server)

* **¿Para qué sirve?**

  Bueno Eureka funciona como una libreta de contactos en nuestro sistema. En lugar de usar IPs fijas (que cambian siempre), cada microservicio se registra aquí al encenderse para que los demás puedan encontrarlo automáticamente por su nombre.

#### 2) Gateway:

* **¿Para qué sirve?**

  Bueno Gateway es la única puerta de entrada de internet. El usuario no se conecta a cada microservicio por separado. Todas las peticiones llegan al Gateway y este las redirige automáticamente al microservicio que corresponda (como el chat o el foro).

---

## 5) Uso de DTOs.

### ¿Qué son los DTOs?

Bueno los DTOs (Data Transfer Objects) son un "escudo protector" y un paquete temporal de datos. (Significa que cuando se transporta la información por internet es temporal). Sirve para transportar por internet únicamente la información necesaria para la pantalla. (La información que nos aparece cuando queremos iniciar sesión en algún sitio random).

### ¿Para qué sirven?
Bueno es por la seguridad que nos brinda (para no exponer datos privados de la base de datos, como contraseñas), y para que tengamos una mayor fluidez al momento de ingresar nuestros datos y no sobrecargar la red.

---

Bueno profe trate de explicar de una manera sencilla y más directa mi informe, aprendí mucho con mis compañeros al momento de ir creando el proyecto y es genial como va agarrando forma.  
Espero que le guste y estaré atento a sus comentarios.

```