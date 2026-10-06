package com.parqueadero.Parqueadero.repository;

import com.parqueadero.Parqueadero.domain.Motocicleta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MotocicletaRepository extends JpaRepository<Motocicleta, UUID> {
}
