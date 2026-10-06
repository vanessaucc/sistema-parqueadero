package com.parqueadero.Parqueadero.controller;

import jakarta.validation.Valid;
import com.parqueadero.Parqueadero.dto.request.CrearTiqueteRequest;
import com.parqueadero.Parqueadero.dto.response.TiqueteResponse;
import com.parqueadero.Parqueadero.service.TiqueteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tiquetes")
public class TiqueteController {

    private final TiqueteService tiqueteService;

    public TiqueteController(TiqueteService tiqueteService) {
        this.tiqueteService = tiqueteService;
    }

    @PostMapping
    public ResponseEntity<TiqueteResponse> crear(
            @Valid @RequestBody CrearTiqueteRequest request,
            UriComponentsBuilder uriBuilder) {
        TiqueteResponse creada = tiqueteService.crear(request);
        URI ruta = uriBuilder.path("/api/tiquetes/{id}").buildAndExpand(creada.id()).toUri();
        return ResponseEntity.created(ruta).body(creada);
    }

    @GetMapping
    public ResponseEntity<List<TiqueteResponse>> listar() {
        return ResponseEntity.ok(tiqueteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TiqueteResponse> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(tiqueteService.obtenerPorId(id));
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<TiqueteResponse> finalizar(
            @PathVariable UUID id,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaSalida) {
        return ResponseEntity.ok(tiqueteService.finalizar(id, fechaSalida));
    }
}
