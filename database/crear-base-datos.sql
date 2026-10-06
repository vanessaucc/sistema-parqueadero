-- =====================================================================
-- Crea la base de datos del parqueadero (solo la base; las tablas las
-- crea Hibernate automaticamente al arrancar la app: ddl-auto=update).
--
-- Opcion 1 (psql, como superusuario):
--   psql -U postgres -f database/crear-base-datos.sql
--
-- Opcion 2 (pgAdmin > Query Tool, conectado a la base "postgres"):
--   ejecutar solo la linea CREATE DATABASE de abajo.
-- =====================================================================

CREATE DATABASE parqueadero_db WITH ENCODING 'UTF8';
