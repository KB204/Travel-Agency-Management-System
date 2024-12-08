package ma.emsi.volservice.dto.airline;

import jakarta.validation.constraints.NotEmpty;


public class AirlineRequest {
    @NotEmpty(message = "Le nom de compagnie aérienne est obligatoire ")
    private String name;
    @NotEmpty(message = "ICAO code est obligatoire")
    private String icaoCode;
    @NotEmpty(message = "Le siège social est obligatoire")
    private String headquarters;
    @NotEmpty(message = "Entrer la capacite d'avion")
    private Integer fleetSize;

    public AirlineRequest(Integer fleetSize, String headquarters, String icaoCode, String name) {
        this.fleetSize = fleetSize;
        this.headquarters = headquarters;
        this.icaoCode = icaoCode;
        this.name = name;
    }

    public AirlineRequest() {
    }

    public String getName() {
        return name;
    }

    public String getIcaoCode() {
        return icaoCode;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public Integer getFleetSize() {
        return fleetSize;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcaoCode(String icaoCode) {
        this.icaoCode = icaoCode;
    }

    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }

    public void setFleetSize(Integer fleetSize) {
        this.fleetSize = fleetSize;
    }

    @Override
    public String toString() {
        return "AirlineRequest{" +
                "name='" + name + '\'' +
                ", icaoCode='" + icaoCode + '\'' +
                ", headquarters='" + headquarters + '\'' +
                ", fleetSize=" + fleetSize +
                '}';
    }
}
