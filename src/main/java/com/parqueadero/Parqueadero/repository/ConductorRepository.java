package com.parqueadero.Parqueadero.repository;

import com.parqueadero.Parqueadero.domain.Conductor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConductorRepository extends JpaRepository<Conductor, UUID> {
    Optional<Conductor> findByIdentificacion(String identificacion);

    boolean existsByIdentificacion(String identificacion);
}
