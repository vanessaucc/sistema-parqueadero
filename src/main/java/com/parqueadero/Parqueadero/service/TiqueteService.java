package com.parqueadero.Parqueadero.service;

import com.parqueadero.Parqueadero.domain.Conductor;
import com.parqueadero.Parqueadero.domain.EstadoTiquete;
import com.parqueadero.Parqueadero.domain.Tiquete;
import com.parqueadero.Parqueadero.domain.Vehiculo;
import com.parqueadero.Parqueadero.dto.request.CrearTiqueteRequest;
import com.parqueadero.Parqueadero.dto.response.TiqueteResponse;
import com.parqueadero.Parqueadero.exception.ConflictoException;
import com.parqueadero.Parqueadero.exception.RecursoNoEncontradoException;
import com.parqueadero.Parqueadero.mapper.TiqueteMapper;
import com.parqueadero.Parqueadero.repository.ConductorRepository;
import com.parqueadero.Parqueadero.repository.TiqueteRepository;
import com.parqueadero.Parqueadero.repository.VehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TiqueteService {

    private final TiqueteRepository tiqueteRepository;
    private final ConductorRepository conductorRepository;
    private final VehiculoRepository vehiculoRepository;
    private final DisponibilidadService disponibilidadService;
    private final TiqueteMapper tiqueteMapper;

    public TiqueteService(TiqueteRepository tiqueteRepository,
                          ConductorRepository conductorRepository,
                          VehiculoRepository vehiculoRepository,
                          DisponibilidadService disponibilidadService,
                          TiqueteMapper tiqueteMapper) {
        this.tiqueteRepository = tiqueteRepository;
        this.conductorRepository = conductorRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.disponibilidadService = disponibilidadService;
        this.tiqueteMapper = tiqueteMapper;
    }

    @Transactional
    public TiqueteResponse crear(CrearTiqueteRequest request) {
        Conductor conductor = conductorRepository.findById(request.conductorId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Conductor no encontrado con ID: " + request.conductorId()));

        Vehiculo vehiculo = vehiculoRepository.findById(request.vehiculoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Vehículo no encontrado con ID: " + request.vehiculoId()));

        if (!conductor.isActivo()) {
            throw new ConflictoException("El conductor se encuentra inactivo");
        }

        if (!vehiculo.getConductor().getId().equals(conductor.getId())) {
            throw new IllegalArgumentException("El vehículo no pertenece al conductor indicado");
        }

        if (tiqueteRepository.existsByVehiculoIdAndEstado(vehiculo.getId(), EstadoTiquete.ACTIVO)) {
            throw new ConflictoException("El vehículo ya cuenta con un tiquete activo en el parqueadero");
        }

        if (!disponibilidadService.hayEspacioPara(vehiculo)) {
            throw new ConflictoException("No hay celdas disponibles para vehículos de tipo " + vehiculo.getTipoVehiculo());
        }

        Tiquete nuevoTiquete = new Tiquete(conductor, vehiculo, request.fechaIngreso());
        Tiquete guardado = tiqueteRepository.save(nuevoTiquete);

        return tiqueteMapper.toResponse(guardado);
    }

    @Transactional
    public TiqueteResponse finalizar(UUID tiqueteId, LocalDateTime fechaSalida) {
        Tiquete tiquete = tiqueteRepository.findById(tiqueteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tiquete no encontrado con ID: " + tiqueteId));

        tiquete.finalizar(fechaSalida);
        return tiqueteMapper.toResponse(tiqueteRepository.save(tiquete));
    }

    @Transactional(readOnly = true)
    public List<TiqueteResponse> listarTodos() {
        return tiqueteMapper.toResponseList(tiqueteRepository.findAll());
    }

    @Transactional(readOnly = true)
    public TiqueteResponse obtenerPorId(UUID id) {
        Tiquete tiquete = tiqueteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tiquete no encontrado con ID: " + id));
        return tiqueteMapper.toResponse(tiquete);
    }
}
