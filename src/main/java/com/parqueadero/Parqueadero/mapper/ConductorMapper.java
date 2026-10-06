package com.parqueadero.Parqueadero.mapper;

import com.parqueadero.Parqueadero.domain.Conductor;
import com.parqueadero.Parqueadero.domain.Tiquete;
import com.parqueadero.Parqueadero.dto.request.ActualizarConductorRequest;
import com.parqueadero.Parqueadero.dto.response.ConductorResponse;
import com.parqueadero.Parqueadero.dto.response.ConductorResumenResponse;
import com.parqueadero.Parqueadero.dto.response.TiqueteItemResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ConductorMapper {

    ConductorResponse toResponse(Conductor conductor);

    List<ConductorResponse> toResponseList(List<Conductor> conductores);

    @Mapping(target = "totalTiquetesRealizados", expression = "java(conductor.getTiquetes() != null ? conductor.getTiquetes().size() : 0)")
    @Mapping(target = "montoTotalGastado", expression = "java(calcularMontoTotal(conductor.getTiquetes()))")
    @Mapping(target = "historialTiquetes", source = "tiquetes", qualifiedByName = "mapTiquetesList")
    ConductorResumenResponse toResumenResponse(Conductor conductor);

    @Named("mapTiquetesList")
    default List<TiqueteItemResponse> mapTiquetesList(List<Tiquete> tiquetes) {
        if (tiquetes == null) {
            return List.of();
        }
        return tiquetes.stream()
                .map(t -> new TiqueteItemResponse(
                        t.getId(),
                        t.getVehiculo() != null ? t.getVehiculo().getPlaca() : null,
                        t.getVehiculo() != null ? t.getVehiculo().getTipoVehiculo() : null,
                        t.getPeriodo() != null ? t.getPeriodo().fechaIngreso() : null,
                        t.getPeriodo() != null ? t.getPeriodo().fechaSalida() : null,
                        t.getEstado(),
                        t.getCobroFinal()))
                .toList();
    }

    default double calcularMontoTotal(List<Tiquete> tiquetes) {
        if (tiquetes == null || tiquetes.isEmpty()) {
            return 0.0;
        }
        return tiquetes.stream()
                .mapToDouble(Tiquete::getCobroFinal)
                .sum();
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "identificacion", ignore = true)
    @Mapping(target = "activo", ignore = true)
    void updateConductorFromDto(ActualizarConductorRequest dto, @MappingTarget Conductor entity);
}
