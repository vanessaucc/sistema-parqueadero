-- Consultas para verificar en pgAdmin / psql (conectado a parqueadero_db)
-- después de ejecutar la colección de Postman.

-- Tablas creadas por Hibernate
SELECT table_name FROM information_schema.tables
WHERE table_schema = 'public' ORDER BY table_name;

-- Herencia JOINED: vehiculos + subtabla según el tipo
SELECT v.placa, v.marca, v.modelo, a.numero_puertas, a.es_electrico
FROM vehiculos v JOIN automoviles a ON a.id = v.id;

SELECT v.placa, v.marca, v.modelo, m.cilindraje, m.necesita_casillero_casco
FROM vehiculos v JOIN motocicletas m ON m.id = v.id;

-- Tiquetes con su estado y cobro
SELECT t.id, c.nombre, v.placa, t.estado, t.fecha_ingreso, t.fecha_salida, t.cobro_final
FROM tiquetes t
JOIN conductores c ON c.id = t.conductor_id
JOIN vehiculos v ON v.id = t.vehiculo_id
ORDER BY t.fecha_ingreso DESC;

-- Incidentes registrados
SELECT i.fecha_hora, i.tipo, v.placa, i.descripcion
FROM incidentes i
JOIN tiquetes t ON t.id = i.tiquete_id
JOIN vehiculos v ON v.id = t.vehiculo_id
ORDER BY i.fecha_hora DESC;

-- Ocupación actual (debe coincidir con GET /api/parqueadero/disponibilidad)
SELECT CASE WHEN a.id IS NOT NULL THEN 'AUTOMOVIL' ELSE 'MOTOCICLETA' END AS tipo,
       COUNT(*) AS ocupadas
FROM tiquetes t
JOIN vehiculos v ON v.id = t.vehiculo_id
LEFT JOIN automoviles a ON a.id = v.id
WHERE t.estado = 'ACTIVO'
GROUP BY 1;
