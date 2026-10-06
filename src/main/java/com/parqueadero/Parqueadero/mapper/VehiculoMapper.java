package com.parqueadero.Parqueadero.mapper;

import com.parqueadero.Parqueadero.domain.Automovil;
import com.parqueadero.Parqueadero.domain.Motocicleta;
import com.parqueadero.Parqueadero.domain.Vehiculo;
import com.parqueadero.Parqueadero.dto.response.AutomovilResponse;
import com.parqueadero.Parqueadero.dto.response.MotocicletaResponse;
import com.parqueadero.Parqueadero.dto.response.VehiculoResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehiculoMapper {

    @Mapping(target = "conductorId", source = "conductor.id")
    @Mapping(target = "nombreConductor", source = "conductor.nombre")
    AutomovilResponse toAutomovilResponse(Automovil entity);

    @Mapping(target = "conductorId", source = "conductor.id")
    @Mapping(target = "nombreConductor", source = "conductor.nombre")
    MotocicletaResponse toMotocicletaResponse(Motocicleta entity);

    default VehiculoResponse toResponse(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return null;
        }
        if (vehiculo instanceof Automovil automovil) {
            return toAutomovilResponse(automovil);
        }
        if (vehiculo instanceof Motocicleta motocicleta) {
            return toMotocicletaResponse(motocicleta);
        }
        throw new IllegalArgumentException("Tipo de vehículo no soportado: " + vehiculo.getClass().getName());
    }

    default List<VehiculoResponse> toResponseList(List<Vehiculo> vehiculos) {
        if (vehiculos == null) {
            return List.of();
        }
        return vehiculos.stream().map(this::toResponse).toList();
    }
}
