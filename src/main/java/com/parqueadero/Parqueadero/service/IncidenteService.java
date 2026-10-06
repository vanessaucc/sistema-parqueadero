package com.parqueadero.Parqueadero.service;

import com.parqueadero.Parqueadero.domain.Incidente;
import com.parqueadero.Parqueadero.domain.Tiquete;
import com.parqueadero.Parqueadero.dto.request.CrearIncidenteRequest;
import com.parqueadero.Parqueadero.dto.response.IncidenteResponse;
import com.parqueadero.Parqueadero.exception.RecursoNoEncontradoException;
import com.parqueadero.Parqueadero.mapper.IncidenteMapper;
import com.parqueadero.Parqueadero.repository.IncidenteRepository;
import com.parqueadero.Parqueadero.repository.TiqueteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class IncidenteService {

    private final IncidenteRepository incidenteRepository;
    private final TiqueteRepository tiqueteRepository;
    private final IncidenteMapper incidenteMapper;

    public IncidenteService(IncidenteRepository incidenteRepository,
                            TiqueteRepository tiqueteRepository,
                            IncidenteMapper incidenteMapper) {
        this.incidenteRepository = incidenteRepository;
        this.tiqueteRepository = tiqueteRepository;
        this.incidenteMapper = incidenteMapper;
    }

    @Transactional
    public IncidenteResponse registrar(CrearIncidenteRequest request) {
        Tiquete tiquete = tiqueteRepository.findById(request.tiqueteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Tiquete no encontrado con ID: " + request.tiqueteId()));

        Incidente incidente = new Incidente(
                tiquete,
                request.tipo(),
                request.descripcion(),
                request.fechaHora()
        );

        Incidente guardado = incidenteRepository.save(incidente);
        return incidenteMapper.toResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<IncidenteResponse> listarTodos() {
        return incidenteMapper.toResponseList(incidenteRepository.findAll());
    }

    @Transactional(readOnly = true)
    public IncidenteResponse obtenerPorId(UUID id) {
        Incidente incidente = incidenteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Incidente no encontrado con ID: " + id));
        return incidenteMapper.toResponse(incidente);
    }
}
