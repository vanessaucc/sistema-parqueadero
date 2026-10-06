package com.parqueadero.Parqueadero.repository;

import com.parqueadero.Parqueadero.domain.Incidente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IncidenteRepository extends JpaRepository<Incidente, UUID> {
    List<Incidente> findByTiqueteId(UUID tiqueteId);
}
