package com.parqueadero.Parqueadero.mapper;

import com.parqueadero.Parqueadero.domain.Incidente;
import com.parqueadero.Parqueadero.dto.response.IncidenteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IncidenteMapper {

    @Mapping(target = "tiqueteId", source = "tiquete.id")
    @Mapping(target = "placaVehiculo", source = "tiquete.vehiculo.placa")
    IncidenteResponse toResponse(Incidente incidente);

    List<IncidenteResponse> toResponseList(List<Incidente> incidentes);
}
