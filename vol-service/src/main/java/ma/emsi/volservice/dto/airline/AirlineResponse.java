package ma.emsi.volservice.dto.airline;


public record AirlineResponse(
        Long id,
        String name,
        String code,
        String headquarters,
        Integer fleetSize) {}
