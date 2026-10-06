package com.parqueadero.Parqueadero.controller;

import jakarta.validation.Valid;
import com.parqueadero.Parqueadero.dto.request.CrearIncidenteRequest;
import com.parqueadero.Parqueadero.dto.response.IncidenteResponse;
import com.parqueadero.Parqueadero.service.IncidenteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/incidentes")
public class IncidenteController {

    private final IncidenteService incidenteService;

    public IncidenteController(IncidenteService incidenteService) {
        this.incidenteService = incidenteService;
    }

    @PostMapping
    public ResponseEntity<IncidenteResponse> registrar(@Valid @RequestBody CrearIncidenteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(incidenteService.registrar(request));
    }

    @GetMapping
    public ResponseEntity<List<IncidenteResponse>> listar() {
        return ResponseEntity.ok(incidenteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidenteResponse> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(incidenteService.obtenerPorId(id));
    }
}
