package ma.emsi.volservice.mapper;

import ma.emsi.volservice.dto.convention.ConventionRequest;
import ma.emsi.volservice.dto.convention.ConventionResponse;
import ma.emsi.volservice.model.Convention;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConventionMapper {
    ConventionResponse conventionToDtoResponse(Convention convention);
    Convention requestDtoToConvention(ConventionRequest request);
}
