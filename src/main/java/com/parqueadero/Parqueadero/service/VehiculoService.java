package com.parqueadero.Parqueadero.service;

import com.parqueadero.Parqueadero.domain.Automovil;
import com.parqueadero.Parqueadero.domain.Conductor;
import com.parqueadero.Parqueadero.domain.Motocicleta;
import com.parqueadero.Parqueadero.domain.Vehiculo;
import com.parqueadero.Parqueadero.dto.request.CrearAutomovilRequest;
import com.parqueadero.Parqueadero.dto.request.CrearMotocicletaRequest;
import com.parqueadero.Parqueadero.dto.response.VehiculoResponse;
import com.parqueadero.Parqueadero.exception.ConflictoException;
import com.parqueadero.Parqueadero.exception.RecursoNoEncontradoException;
import com.parqueadero.Parqueadero.mapper.VehiculoMapper;
import com.parqueadero.Parqueadero.repository.ConductorRepository;
import com.parqueadero.Parqueadero.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ConductorRepository conductorRepository;
    private final VehiculoMapper vehiculoMapper;

    public VehiculoService(VehiculoRepository vehiculoRepository,
                           ConductorRepository conductorRepository,
                           VehiculoMapper vehiculoMapper) {
        this.vehiculoRepository = vehiculoRepository;
        this.conductorRepository = conductorRepository;
        this.vehiculoMapper = vehiculoMapper;
    }

    @Transactional
    public VehiculoResponse crearAutomovil(CrearAutomovilRequest request) {
        if (vehiculoRepository.existsByPlaca(request.placa().trim().toUpperCase())) {
            throw new ConflictoException("Ya existe un vehículo registrado con la placa: " + request.placa().trim().toUpperCase());
        }
        Conductor conductor = conductorRepository.findById(request.conductorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Conductor no encontrado con ID: " + request.conductorId()));

        Automovil automovil = new Automovil(
                request.placa(),
                request.marca(),
                request.modelo(),
                request.tarifaPorHora(),
                conductor,
                request.numeroPuertas(),
                request.esElectrico()
        );
        return vehiculoMapper.toResponse(vehiculoRepository.save(automovil));
    }

    @Transactional
    public VehiculoResponse crearMotocicleta(CrearMotocicletaRequest request) {
        if (vehiculoRepository.existsByPlaca(request.placa().trim().toUpperCase())) {
            throw new ConflictoException("Ya existe un vehículo registrado con la placa: " + request.placa().trim().toUpperCase());
        }
        Conductor conductor = conductorRepository.findById(request.conductorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Conductor no encontrado con ID: " + request.conductorId()));

        Motocicleta motocicleta = new Motocicleta(
                request.placa(),
                request.marca(),
                request.modelo(),
                request.tarifaPorHora(),
                conductor,
                request.cilindraje(),
                request.necesitaCasilleroCasco()
        );
        return vehiculoMapper.toResponse(vehiculoRepository.save(motocicleta));
    }

    @Transactional(readOnly = true)
    public List<VehiculoResponse> obtenerTodos() {
        return vehiculoRepository.findAll().stream()
                .map(vehiculoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public VehiculoResponse obtenerPorId(UUID id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo no encontrado con ID: " + id));
        return vehiculoMapper.toResponse(vehiculo);
    }
}
