package com.parqueadero.Parqueadero.controller;

import com.parqueadero.Parqueadero.dto.response.DisponibilidadResponse;
import com.parqueadero.Parqueadero.service.DisponibilidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parqueadero")
public class DisponibilidadController {

    private final DisponibilidadService disponibilidadService;

    public DisponibilidadController(DisponibilidadService disponibilidadService) {
        this.disponibilidadService = disponibilidadService;
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<DisponibilidadResponse> consultarDisponibilidad() {
        return ResponseEntity.ok(disponibilidadService.consultarDisponibilidad());
    }
}
