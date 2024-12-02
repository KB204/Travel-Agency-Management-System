package net.travelsystem.hotelservice.mapper;

import net.travelsystem.hotelservice.dto.convention.ConventionResponse;
import net.travelsystem.hotelservice.entities.Convention;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConventionMapper {

    ConventionResponse conventionToDtoResponse(Convention convention);
}
