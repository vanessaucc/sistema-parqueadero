package com.parqueadero.Parqueadero.dto.response;

public record DisponibilidadResponse(
        int capacidadTotal,
        int celdasOcupadas,
        int espaciosLibres,
        int capacidadAutomoviles,
        int ocupadasAutomoviles,
        int disponiblesAutomoviles,
        int capacidadMotocicletas,
        int ocupadasMotocicletas,
        int disponiblesMotocicletas) {
}
