package com.parqueadero.Parqueadero.mapper;

import com.parqueadero.Parqueadero.domain.Tiquete;
import com.parqueadero.Parqueadero.dto.response.TiqueteResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TiqueteMapper {

    @Mapping(target = "conductorId", source = "conductor.id")
    @Mapping(target = "nombreConductor", source = "conductor.nombre")
    @Mapping(target = "vehiculoId", source = "vehiculo.id")
    @Mapping(target = "placaVehiculo", source = "vehiculo.placa")
    @Mapping(target = "tipoVehiculo", expression = "java(tiquete.getVehiculo() != null ? tiquete.getVehiculo().getTipoVehiculo() : null)")
    @Mapping(target = "fechaIngreso", expression = "java(tiquete.getPeriodo() != null ? tiquete.getPeriodo().fechaIngreso() : null)")
    @Mapping(target = "fechaSalida", expression = "java(tiquete.getPeriodo() != null ? tiquete.getPeriodo().fechaSalida() : null)")
    TiqueteResponse toResponse(Tiquete tiquete);

    List<TiqueteResponse> toResponseList(List<Tiquete> tiquetes);
}
