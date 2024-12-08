package ma.emsi.volservice.dto.flight;

import jakarta.validation.constraints.NotEmpty;
import ma.emsi.volservice.enums.FlightType;
import ma.emsi.volservice.model.Airline;

import java.time.LocalDateTime;

public class FlightRequest {
        @NotEmpty(message = "Le nom de airline est obligatoire")
        private String airlineName;
        @NotEmpty(message = "Le icao est obligatoire")
        private String icaoCode;
        @NotEmpty(message = "Le numero de vol est obligatoire")
        private String flightNo;
        @NotEmpty(message = "L'origine de vol est obligatoire")
        private String origin;
        @NotEmpty(message = "La destination de vol est obligatoire")
        private String destination;
        @NotEmpty(message = "le type de vol est obligatoire")
        private FlightType flightType;
        @NotEmpty(message = "La date de vol est obligatoire")
        private LocalDateTime departureTime;
        private LocalDateTime arrivalTime;

        public FlightRequest(String airlineName, String icaoCode, String flightNo, String origin, String destination, FlightType flightType, LocalDateTime departureTime, LocalDateTime arrivalTime) {
                this.airlineName = airlineName;
                this.icaoCode = icaoCode;
                this.flightNo = flightNo;
                this.origin = origin;
                this.destination = destination;
                this.flightType = flightType;
                this.departureTime = departureTime;
                this.arrivalTime = arrivalTime;
        }

        public FlightRequest() {
        }

        public String getAirlineName() {
                return airlineName;
        }
        public void setAirlineName(String airlineName) {
                this.airlineName = airlineName;
        }
        public String getIcaoCode() {
                return icaoCode;
        }
        public void setIcaoCode(String icaoCode) {
                this.icaoCode = icaoCode;
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
