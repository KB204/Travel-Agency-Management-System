package ma.emsi.volservice.dto.airline;


public class AirlineResponse {
    private Long id;
    private String name;
    private String icao;
    private String headquarters;
    private Integer fleetSize;

    public AirlineResponse(Long id, String name, String icao, String headquarters, Integer fleetSize) {
        this.id = id;
        this.name = name;
        this.icao = icao;
        this.headquarters = headquarters;
        this.fleetSize = fleetSize;
    }

    public AirlineResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcao() {
        return icao;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Integer getFleetSize() {
        return fleetSize;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public void setFleetSize(Integer fleetSize) {
        this.fleetSize = fleetSize;
    }
}
