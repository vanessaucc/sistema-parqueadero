package com.parqueadero.Parqueadero.repository;

import com.parqueadero.Parqueadero.domain.Automovil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AutomovilRepository extends JpaRepository<Automovil, UUID> {
}
