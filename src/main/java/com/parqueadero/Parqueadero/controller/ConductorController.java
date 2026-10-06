package com.parqueadero.Parqueadero.controller;

import jakarta.validation.Valid;
import com.parqueadero.Parqueadero.dto.request.ActualizarConductorRequest;
import com.parqueadero.Parqueadero.dto.request.CrearConductorRequest;
import com.parqueadero.Parqueadero.dto.response.ConductorResponse;
import com.parqueadero.Parqueadero.dto.response.ConductorResumenResponse;
import com.parqueadero.Parqueadero.service.ConductorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/conductores")
public class ConductorController {

    private final ConductorService conductorService;

    public ConductorController(ConductorService conductorService) {
        this.conductorService = conductorService;
    }

    @PostMapping
    public ResponseEntity<ConductorResponse> crear(@Valid @RequestBody CrearConductorRequest request,
                                                   UriComponentsBuilder uriBuilder) {
        ConductorResponse response = conductorService.crear(request);
        URI uri = uriBuilder.path("/api/conductores/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ConductorResponse>> listar() {
        return ResponseEntity.ok(conductorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConductorResponse> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(conductorService.obtenerPorId(id));
    }

    @GetMapping("/{id}/resumen")
    public ResponseEntity<ConductorResumenResponse> obtenerResumen(@PathVariable UUID id) {
        return ResponseEntity.ok(conductorService.obtenerResumen(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ConductorResponse> actualizarParcial(
            @PathVariable UUID id,
            @Valid @RequestBody ActualizarConductorRequest request) {
        return ResponseEntity.ok(conductorService.actualizarParcial(id, request));
    }
}
