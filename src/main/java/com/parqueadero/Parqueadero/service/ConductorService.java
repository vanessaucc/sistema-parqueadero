package com.parqueadero.Parqueadero.service;

import com.parqueadero.Parqueadero.domain.Conductor;
import com.parqueadero.Parqueadero.dto.request.ActualizarConductorRequest;
import com.parqueadero.Parqueadero.dto.request.CrearConductorRequest;
import com.parqueadero.Parqueadero.dto.response.ConductorResponse;
import com.parqueadero.Parqueadero.dto.response.ConductorResumenResponse;
import com.parqueadero.Parqueadero.exception.ConflictoException;
import com.parqueadero.Parqueadero.exception.RecursoNoEncontradoException;
import com.parqueadero.Parqueadero.mapper.ConductorMapper;
import com.parqueadero.Parqueadero.repository.ConductorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ConductorService {

    private final ConductorRepository conductorRepository;
    private final ConductorMapper conductorMapper;

    public ConductorService(ConductorRepository conductorRepository, ConductorMapper conductorMapper) {
        this.conductorRepository = conductorRepository;
        this.conductorMapper = conductorMapper;
    }

    @Transactional
    public ConductorResponse crear(CrearConductorRequest request) {
        String identificacion = request.identificacion().trim();
        if (conductorRepository.existsByIdentificacion(identificacion)) {
            throw new ConflictoException("Ya existe un conductor con la identificación: " + identificacion);
        }
        Conductor conductor = new Conductor(
                request.nombre(),
                identificacion,
                request.telefono(),
                request.email()
        );
        Conductor guardado = conductorRepository.save(conductor);
        return conductorMapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<ConductorResponse> listarTodos() {
        return conductorMapper.toResponseList(conductorRepository.findAll());
    }

    @Transactional(readOnly = true)
    public ConductorResponse obtenerPorId(UUID id) {
        return conductorMapper.toResponse(buscar(id));
    }

    @Transactional(readOnly = true)
    public ConductorResumenResponse obtenerResumen(UUID id) {
        return conductorMapper.toResumenResponse(buscar(id));
    }

    @Transactional
    public ConductorResponse actualizarParcial(UUID id, ActualizarConductorRequest request) {
        Conductor conductor = buscar(id);

        // MapStruct aplica únicamente los campos no nulos sobre la entidad recuperada de la BD
        conductorMapper.updateConductorFromDto(request, conductor);

        Conductor guardado = conductorRepository.save(conductor);
        return conductorMapper.toResponse(guardado);
    }

    private Conductor buscar(UUID id) {
        return conductorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Conductor no encontrado con ID: " + id));
    }
}
