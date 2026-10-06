# Sistema de Gestión de Parqueadero Inteligente (ParqueaderoSpringBoot)

Spring Boot 3.3 · Java 17 · JPA/Hibernate · PostgreSQL · MapStruct

Integrantes: Julieth Vanessa Mena Ortega · Valeria Estefanía Góngora Torres · Edison Andres Chcua Quistial

## 1. Requisitos

- JDK 17 o superior
- PostgreSQL 14+ (o Docker)
- Postman

## 2. Base de datos PostgreSQL

La app usa por defecto: base `parqueadero_db`, usuario `postgres`, clave `postgres`, puerto `5432`.
Hibernate crea las tablas solo (`spring.jpa.hibernate.ddl-auto=update`); únicamente hay que crear la base vacía.

**Opción A – Docker (más rápida)**

```bash
docker compose up -d
```

**Opción B – PostgreSQL instalado**

```bash
psql -U postgres -f database/crear-base-datos.sql
```

o en pgAdmin: clic derecho en *Databases → Create → Database…* → nombre `parqueadero_db`.

**¿Tu usuario o clave son otros?** No edites el código; usa variables de entorno:

```bash
# Linux / Mac
export DB_USERNAME=mi_usuario DB_PASSWORD=mi_clave
# PowerShell
$env:DB_USERNAME="mi_usuario"; $env:DB_PASSWORD="mi_clave"
```

(también `DB_URL`, por ejemplo `jdbc:postgresql://localhost:5433/parqueadero_db`).

## 3. Ejecutar

```bash
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

Queda en `http://localhost:8080`. Al arrancar se crean las tablas
`conductores, vehiculos, automoviles, motocicletas, tiquetes, incidentes`.
`database/consultas-verificacion.sql` trae consultas para comprobarlo en pgAdmin.

## 4. Postman

1. *Import* → `postman/Parqueadero_Postman_Collection.json`.
2. Con la app corriendo, abre la colección → **Run** → ejecuta todo en orden.
   - Los IDs se guardan solos en variables de colección (`conductorId`, `tiqueteAutoId`, …).
   - Identificaciones y placas se generan únicas, así que se puede correr varias veces.
   - Cada request trae tests: verde = el contrato se cumple.
3. La carpeta **9. Casos de error** comprueba los 400 / 404 / 409.

También hay `solicitudes-avanzadas.http` para VS Code (REST Client) / IntelliJ.

## 5. Contratos REST

| Módulo | Verbo | Endpoint | Respuesta |
|---|---|---|---|
| Disponibilidad | GET | `/api/parqueadero/disponibilidad` | 200 |
| Vehículos | POST | `/api/vehiculos/automoviles` | 201 |
| Vehículos | POST | `/api/vehiculos/motocicletas` | 201 |
| Vehículos | GET | `/api/vehiculos` · `/api/vehiculos/{id}` | 200 |
| Conductores | POST / GET | `/api/conductores` · `/api/conductores/{id}` | 201 / 200 |
| Conductores | GET | `/api/conductores/{id}/resumen` | 200 |
| Conductores | PATCH | `/api/conductores/{id}` | 200 |
| Tiquetes | POST | `/api/tiquetes` (ingreso) | 201 |
| Tiquetes | POST | `/api/tiquetes/{id}/finalizar[?fechaSalida=ISO]` | 200 |
| Tiquetes | GET | `/api/tiquetes` · `/api/tiquetes/{id}` | 200 |
| Incidentes | POST | `/api/incidentes` | 201 |
| Incidentes | GET | `/api/incidentes` · `/api/incidentes/{id}` | 200 |

### Errores (cuerpo JSON uniforme)

```json
{ "timestamp": "...", "status": 409, "error": "Conflict",
  "mensaje": "Ya existe un conductor con la identificación: 123", "ruta": "/api/conductores", "detalles": null }
```

| Código | Cuándo |
|---|---|
| 400 | Validación de campos, JSON/UUID/enum/fecha inválidos, vehículo que no es del conductor |
| 404 | Conductor, vehículo, tiquete o incidente inexistente |
| 409 | Identificación o placa duplicada, tiquete ya activo/finalizado, parqueadero lleno para ese tipo |

## 6. Reglas de negocio

- Al crear un tiquete se valida: conductor activo, el vehículo pertenece al conductor, el vehículo no tiene otro tiquete ACTIVO y **hay celdas libres** para su tipo (capacidad en `application.properties`).
- Cobro al finalizar: `tarifaPorHora × horas` redondeando hacia arriba, mínimo 1 hora.
- PATCH de conductor solo acepta `nombre`, `telefono`, `email`; `identificacion` y `activo` se ignoran (anti asignación masiva).

## 7. Tests

```bash
./mvnw test
```

Usan H2 en memoria (`application-test.properties`), no tocan PostgreSQL.
