package com.parqueadero.Parqueadero.repository;

import com.parqueadero.Parqueadero.domain.EstadoTiquete;
import com.parqueadero.Parqueadero.domain.Tiquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TiqueteRepository extends JpaRepository<Tiquete, UUID> {

    List<Tiquete> findByConductorId(UUID conductorId);

    long countByEstado(EstadoTiquete estado);

    @Query("SELECT COUNT(t) FROM Tiquete t WHERE t.estado = :estado AND TYPE(t.vehiculo) = com.parqueadero.Parqueadero.domain.Automovil")
    long countOcupadasAutomoviles(@Param("estado") EstadoTiquete estado);

    @Query("SELECT COUNT(t) FROM Tiquete t WHERE t.estado = :estado AND TYPE(t.vehiculo) = com.parqueadero.Parqueadero.domain.Motocicleta")
    long countOcupadasMotocicletas(@Param("estado") EstadoTiquete estado);

    boolean existsByVehiculoIdAndEstado(UUID vehiculoId, EstadoTiquete estado);
}
