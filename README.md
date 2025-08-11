# Aplicación de Gestión de Libros y Autores con Spring Boot 📚

---

Este proyecto es una aplicación de consola en **Java** y **Spring Boot** para la gestión de un catálogo digital de libros y autores. Demuestra la integración con la **API de Gutendex**, la persistencia de datos con **JPA** y el uso de consultas avanzadas en un entorno modular y robusto.

---

## Características Clave y Funcionalidades ✨

La aplicación ofrece un menú interactivo con opciones que cubren el ciclo completo de gestión de datos:

* ### **Búsqueda y Registro en la API**
  * Permite buscar libros individuales por título o todos los libros de un autor en la API de Gutendex.
  * Los datos obtenidos se registran automáticamente en la base de datos local, evitando duplicados.

* ### **Persistencia y Consultas Avanzadas**
  * **Listado de Datos**: Muestra listas de todos los libros y autores registrados en la base de datos.
  * **JPQL Personalizadas**: Utiliza consultas JPQL para operaciones complejas como:
    * Listar autores que estaban vivos en un año específico.
    * Mostrar el Top 10 de libros más descargados.
    * Contar el número de libros registrados por idioma y por autor.

* ### **Manejo de Errores y Excepciones**
  * Implementa un manejo de excepciones (`try-catch`) para asegurar que la aplicación no falle ante entradas de usuario no válidas, proporcionando una experiencia de uso estable y fiable.

---

## Tecnologías y Herramientas Utilizadas 🛠️

Este proyecto fue desarrollado utilizando un conjunto de tecnologías modernas y estándares de la industria:

* **Lenguaje de Programación**: Java 17
* **Framework**: Spring Boot 3.x
* **Persistencia**: Spring Data JPA
* **Base de Datos**: PostgreSQL / H2 (configurable)
* **Herramienta de Construcción**: Maven
* **Control de Versiones**: Git & GitHub

---

## Guía de Inicio Rápido 🚀

### **1. Requisitos Previos**
Asegúrate de tener instalados los siguientes componentes:
* **Java Development Kit (JDK)**: Versión 17 o superior.
* **Apache Maven**: Versión 3.6 o superior.
* Un **Editor de Código** (se recomienda IntelliJ IDEA).

### **2. Pasos para la Configuración Local**
**1. Clonar el repositorio:**
   ```bash
   git clone [https://github.com/tu-usuario/nombre-del-repositorio.git](https://github.com/Jess-93/Literatura.git)
   ```
**2. Configurar la Base de Datos:**
   * Abre el archivo `src/main/resources/application.properties`.
   * Ajusta la conexión a tu base de datos de preferencia (por ejemplo, PostgreSQL)
    
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/literatura
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_contraseña
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   ```
   *(Opcional)* Puedes usar una base de datos en memoria (H2) para pruebas sin necesidad de una configuración externa.

### **3. Ejecutar la Aplicación**
1. Abre el proyecto en IntelliJ IDEA.
2. Navega a la clase principal `LiteraturaApplication.java`.
3. Ejecuta la aplicación haciendo clic en el botón `Run` junto al método `main`.
4. El menú principal se mostrará en la consola, listo para que interactúes.

---

## Autor ✍️

* ### Jesús Villarreal
* ### GitHub: https://github.com/Jess-93

## Agradecimientos 🙏

Este proyecto es un desafío de un programa educacional llamado **ONE** financiado por **Oracle**. No es solo código, es el resultado de una oportunidad que ha transformado mi trayectoria. Expreso mi más profunda gratitud a **Oracle** por su visión y por impulsar el programa **Oracle Next Education (ONE)**. También a **Alura Latam,** cuyos instructores, con su pasión y guía, han convertido cada desafío en un aprendizaje invaluable. Esta experiencia ha sido más que una formación; ha sido el inicio de un camino profesional que abrazo con entusiasmo.
¡Muchas gracias a todos!
