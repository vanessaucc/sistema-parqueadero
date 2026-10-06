package com.parqueadero.Parqueadero.controller;

import jakarta.validation.Valid;
import com.parqueadero.Parqueadero.dto.request.CrearAutomovilRequest;
import com.parqueadero.Parqueadero.dto.request.CrearMotocicletaRequest;
import com.parqueadero.Parqueadero.dto.response.VehiculoResponse;
import com.parqueadero.Parqueadero.service.VehiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/vehiculos")
public class VehiculoController {

    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @PostMapping("/automoviles")
    public ResponseEntity<VehiculoResponse> crearAutomovil(@Valid @RequestBody CrearAutomovilRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.crearAutomovil(request));
    }

    @PostMapping("/motocicletas")
    public ResponseEntity<VehiculoResponse> crearMotocicleta(@Valid @RequestBody CrearMotocicletaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.crearMotocicleta(request));
    }

    @GetMapping
    public ResponseEntity<List<VehiculoResponse>> obtenerTodos() {
        return ResponseEntity.ok(vehiculoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculoResponse> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(vehiculoService.obtenerPorId(id));
    }
}
