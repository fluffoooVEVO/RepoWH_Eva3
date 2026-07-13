<<<<<<< HEAD
# RepoWH_Eva3 — Warhammer Figures

Examen Final Transversal — Desarrollo FullStack 1 (DuocUC)
Docente: Byron Andrés Aros Araya
Integrantes: Daniel Mora, Lucas Toledo, Javier Rodriguez

## Descripción del proyecto

Sistema de microservicios para la gestión de un catálogo de figuras coleccionables del universo Warhammer 40K. El proyecto fue desarrollado de forma colaborativa, integrando una rama de trabajo por integrante y consolidando los avances mediante Pull Requests y merges continuos hacia una rama de integración común (`BranchMerge`), utilizando Git y GitBash/VS Code como herramientas de control de versiones.

*(Desarrollo inicial: semana del 04/05/26 al 10/05/26. Integración final y correcciones documentadas en este README.)*

## Arquitectura del sistema y responsables

El proyecto está compuesto por 10 microservicios independientes, cada uno con su propia base de datos MySQL cuando corresponde, registrados en un servidor de descubrimiento (Eureka) y expuestos a través de un API Gateway centralizado. El proyecto inició con 10 microservicios base y fue evolucionando en número y alcance conforme avanzó el desarrollo.

| Servicio | Puerto | Responsable |
|---|---|---|
| Eureka Server | 8761 | Daniel |
| API Gateway | 8090 | Daniel |
| Categorías | 8081 | Lucas |
| Enlaces | 8082 | Lucas |
| Main Producto | 8086 | Daniel |
| ms-edicion | 8092 | Javier |
| ms-figura | 8091 | Javier |


### Links importantes

| Recurso | URL |
|---|---|
| Swagger unificado | `http://localhost:8090/doc/swagger-ui/index.html` |
| Eureka Server | `http://localhost:8761` |
| API Gateway | `http://localhost:8090` |

## Contenidos de la asignatura integrados

Este proyecto integra de forma práctica los siguientes contenidos trabajados durante el curso:

**Persistencia y modelado**
- Entidades JPA/Hibernate con relaciones (`@OneToMany`, `@ManyToOne`) y mapeo a base de datos relacional.
- Normalización del modelo de datos, con desacoplamiento entre microservicios (cada uno guarda solo el `id` de recursos externos, sin claves foráneas cruzadas entre bases de datos distintas).
- Migraciones de esquema versionadas con **Flyway**, garantizando reproducibilidad del estado de la base de datos entre entornos.

**Validación y manejo de errores**
- Bean Validation (`@NotBlank`, `@NotNull`, `@Size`) en los DTOs de entrada.
- Manejo centralizado de excepciones (`@RestControllerAdvice`, `ResponseStatusException`) con respuestas HTTP semánticamente correctas (400, 404, 503).

**Arquitectura REST**
- Separación estricta entre entidades JPA y DTOs de transferencia, evitando exponer el modelo de persistencia directamente en la API.
- Estructura en capas: Controller → Service → Repository.

**HATEOAS**
- Respuestas enriquecidas con hipermedios usando `RepresentationModelAssembler` y `EntityModel<T>`, permitiendo que el cliente navegue la API mediante links (`self` y relaciones a otros recursos) en lugar de construir URLs manualmente.

**Comunicación entre microservicios**
- Clientes `WebClient` (programación reactiva no bloqueante) para consumir datos de otros microservicios en tiempo real.
- Manejo de resiliencia básica: timeouts, distinción entre errores 4xx (recurso no encontrado) y 5xx (servicio caído), y respuesta `503 SERVICE_UNAVAILABLE` cuando un microservicio dependiente no responde.
- Links HATEOAS hacia recursos externos, generados de forma manual (`Link.of()`) dado que la comunicación cruza los límites de la JVM de cada microservicio.

**Descubrimiento de servicios y Gateway**
- Registro y descubrimiento dinámico de servicios con **Spring Cloud Netflix Eureka**.
- Enrutamiento centralizado y configuración de CORS mediante **Spring Cloud Gateway**.

**Documentación de API**
- Documentación OpenAPI/Swagger por microservicio, unificada a través del Gateway.

**Testing**
- Pruebas unitarias con **JUnit 5** y **Mockito** sobre la capa de servicio, mockeando repositorios y clientes externos para aislar la lógica de negocio.

**Logging**
- Logging estructurado con **SLF4J** en la capa de servicio, diferenciando niveles (`info`, `debug`, `error`) según el contexto de la operación.

**Contenerización**
- Dockerfile individual por microservicio, desarrollado por cada integrante para su propio servicio (Lucas: Categorías; Daniel: Enlaces y Main Producto; Javier: ms-edicion y ms-figura).
- `docker-compose.yml` de orquestación general, desarrollado por Lucas: integra los 7 microservicios y 5 bases de datos MySQL en contenedores independientes, conectados por una red bridge común, con `healthcheck` para garantizar el orden correcto de arranque entre servicios dependientes.
- Configuración parametrizada por variables de entorno (`${VARIABLE:localhost}`), permitiendo que el mismo artefacto funcione tanto en ejecución local como dentro de contenedores Docker sin duplicar configuración.

**Control de versiones colaborativo**
- Desarrollo distribuido en ramas por integrante/módulo, integradas de forma continua a una rama común mediante merges verificados (sin conflictos) antes de cada push.
- Convenciones de equipo unificadas (estructura de paquetes, patrón de Assemblers HATEOAS) para mantener consistencia de código entre microservicios desarrollados por distintos integrantes.
