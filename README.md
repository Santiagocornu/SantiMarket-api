SantiMarket API
Este proyecto es una API RESTful desarrollada con Spring Boot para la gestión de un sistema de mercado, incluyendo carritos de compra, productos y usuarios, utilizando PostgreSQL como base de datos, alojada en AWS RDS.

 Tecnologías
Lenguaje: Java 17

Framework: Spring Boot 3.x

Base de Datos: PostgreSQL, aws aurora y rds.

ORM: Spring Data JPA / Hibernate

Dependencias Adicionales: Lombok

Contenedorización: Docker, Docker Compose

Gestor de Dependencias: Maven

 Estructura del Proyecto
La aplicación sigue una arquitectura organizada en paquetes clave:

com.Api.SantiMarket (Paquete Raíz)

Config: Contiene clases de configuración (ej. CorsConfig).

Controller: Maneja las peticiones HTTP y define los endpoints REST.

Entities: Define los modelos de datos (entidades de JPA).

Exceptions: Clases para manejo de excepciones personalizadas.

Interfaces: Define las interfaces de los repositorios y servicios (DAO/Repository y Service).

Service: Contiene la lógica de negocio (ej. ProductosService, CarritoComprasService, UsuariosService).

 Configuración y Ejecución
1. Requisitos Previos
   Asegúrate de tener instalados:

Java Development Kit (JDK) 17

Maven

Docker y Docker Compose

Acceso a la instancia de AWS RDS PostgreSQL (la conexión ya está configurada).

2. Configuración de la Base de Datos
   Tu configuración actual apunta a una instancia de AWS RDS. Los detalles de conexión se encuentran en application.properties:

URL: jdbc:postgresql://santimarket-instance-1.ct2yeo8gu7m9.us-east-2.rds.amazonaws.com:5432/santimarket

Usuario: ------

Contraseña: --------


3. Ejecución Local (Sin Docker)
   Compilar el Proyecto:

Bash

mvn clean package -DskipTests
Ejecutar la Aplicación:

Bash

java -jar target/SantiMarket-0.0.1-SNAPSHOT.jar
La API estará disponible en http://localhost:8080.

4. Ejecución con Docker Compose
   Usaremos Docker Compose para construir la imagen y levantar el contenedor.

Construir y Levantar el Contenedor:

Bash

docker-compose up --build -d
Esto construirá la imagen (Dockerfile) y ejecutará la aplicación.

El puerto 8080 de tu máquina estará mapeado al puerto 8080 del contenedor.

Las variables de entorno de la base de datos se toman de docker-compose.yml (aunque para el URL, se sigue usando la URL completa dentro del application.properties embebido en el JAR).

Verificar Logs:

Bash

docker-compose logs -f app
Detener la Aplicación:

Bash

docker-compose down

Endpoints Principales(especificados dentro de la carpeta README(apiUse))

