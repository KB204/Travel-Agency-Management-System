package ma.emsi.volservice.dto.flight;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import ma.emsi.volservice.enums.FlightType;
import ma.emsi.volservice.model.Airline;

import java.time.LocalDateTime;

public class FlightResponse{
    private String flightNo;
    private String origin;
    private String destination;
    @Enumerated(EnumType.STRING)
    private FlightType flightType;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;

    public FlightResponse(String flightNo, String origin, String destination, FlightType flightType, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.flightNo = flightNo;
        this.origin = origin;
        this.destination = destination;
        this.flightType = flightType;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public FlightResponse() {
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public FlightType getFlightType() {
        return flightType;
    }

    public void setFlightType(FlightType flightType) {
        this.flightType = flightType;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
}
