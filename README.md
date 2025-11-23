# Proyecto Final | Curso Integrador I: Sistemas en Software
###### Programador(es): Montañez Fabrizio · Revilla Ariel 

#### 1. Descripcion General:
##### Introduccion:
Sistema web de gestión de proyectos para empresas de consultoría que permite administrar el ciclo completo de proyectos de clientes, asignar servicios desde un catálogo reutilizable, y gestionar personal.

##### Usuario(s) objetivo:
- Administradores
- Gerentes de Proyecto

#### 2. Caracteristicas Principales:
Lista de los 6 módulos de negocio principales:
- **Gestión de Proyectos**
- **Gestión de Clientes**
- **Catálogo de Servicios**
- **Gestión de Personal**
- **Seguimiento de Asignaciones**
- **Reportes y Analíticas**

#### 3. Stack Tecnologico:
Tecnologias que se emplearon en el proyecto

**Back-End:**
  - ![](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white)  
  - ![](https://img.shields.io/badge/Java-007396?logo=java&logoColor=white)

**Front-End:**  
  -  ![](https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=white)  
  -  ![](https://img.shields.io/badge/Tailwind_CSS-38B2AC?logo=tailwindcss&logoColor=white)

**Base de Datos:**  
  -  ![](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)  
  -  ![](https://img.shields.io/badge/H2-004088?logo=h2&logoColor=white)

**Seguridad:**  
  -  ![](https://img.shields.io/badge/Spring%20Security-6DB33F?logo=springsecurity&logoColor=white)  
  -  ![](https://img.shields.io/badge/BCrypt-000000)

**Testing:**  
  -  ![](https://img.shields.io/badge/JUnit_5-25A162?logo=junit5&logoColor=white)  
  -  ![](https://img.shields.io/badge/Mockito-2A8E6A)  
  -  ![](https://img.shields.io/badge/Testcontainers-3D8EB9?logo=testcontainers&logoColor=white)  
  -  ![](https://img.shields.io/badge/Selenium-43B02A?logo=selenium&logoColor=white)

#### 4. Requisitos Previos:
Se requiere:
- **Java 21** (O posteriores)  
  ![](https://img.shields.io/badge/Java_21-007396?logo=java&logoColor=white)

- **Maven 3.x** (O posteriores)
  ![](https://img.shields.io/badge/Maven_3.x-C71A36?logo=apachemaven&logoColor=white)

- **MySQL 8.x** (O posteriores) 
  ![](https://img.shields.io/badge/MySQL_8.x-4479A1?logo=mysql&logoColor=white)

- **Navegador Web Moderno**  
  ![](https://img.shields.io/badge/Browser-Chrome%20%7C%20Firefox%20%7C%20Edge-4285F4?logo=googlechrome&logoColor=white)

#### 5. Instalacion & Configuracion:
Pasos para configurar la base de datos en la seccion de: **application.properties**:

Comandos de Maven para compilar y ejecutar en terminal:

| Comando(s): | Descripcion(es):                    |
| ------------- | ------------------------------ |
| `mvn clean install`      | Limpia las versiones obsoletas e instala las dependencias mas recientes.       |
| `mvn spring-boot:run`   | Ejecuta el proyecto de forma local para su interaccion.     |

#### 6. Arquitectura del Sistema:
Descripción de la arquitectura en 7 capas:

- Capa de Presentación (**Thymeleaf**)
- Capa Web (**Controllers**)
- Capa de Seguridad (**SecurityFilterChain**)
- Capa de Servicio (**Service**)
- Capa de Repositorio (**JPA**)
- Modelo de Dominio (**Entity**)
- Fuentes de Datos (**MySQL/H2**)

#### 7. Estructura del Proyecto:
Mapeo de controladores a rutas:

| Ruta(s): | Descripcion(es):                    |
| ------------- | ------------------------------ |
| `/proyectos`      | Acceso a proyectos del sistema.       |
| `/servicios`   | Acceso a servicios que se brindan.     |
| `/personal`   | Acceso al personal de la empresa.     |
| `/clientes`   | Acceso a la vista de clientes asociados.     |
| `/login`   | Acceso a capa de inicio de sesion al sistema.     |

#### 8. Testing del Sistema:
Estrategia de pruebas con cobertura mínima del **50%**:

- **Unit Tests:**  
  -  ![](https://img.shields.io/badge/JUnit_5-25A162?logo=junit5&logoColor=white)  
  -  ![](https://img.shields.io/badge/Mockito-2A8E6A)

- **Integration Tests:**  
  -  ![](https://img.shields.io/badge/Testcontainers-3D8EB9?logo=testcontainers&logoColor=white)

- **E2E Tests:**  
  -  ![](https://img.shields.io/badge/Selenium-43B02A?logo=selenium&logoColor=white)

Comando de ejecucion de tests:

`mvn test` + `mvn veriyh`

#### 9. Modelado de Datos:
Diagrama o descripción de las entidades principales:

- **Usuario, Personal, Cliente**
- **Proyecto, Servicio, ProyectoServicio**
- **Asignacion, Requerimiento, Informe**

#### 10. Contribucion:
Proyecto educativo de curso de integracion de tecnologias estudiadas en **seis (06) ciclos**.

#### 11. Contacto/Soporte:
Información de contacto o enlaces relevantes:

**Correos Institucionales** 
- U23250775@utp.edu.pe
- U20212197@utp.edu.pe
