package com.parqueadero.Parqueadero.repository;

import com.parqueadero.Parqueadero.domain.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface VehiculoRepository extends JpaRepository<Vehiculo, UUID> {
    Optional<Vehiculo> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
}
