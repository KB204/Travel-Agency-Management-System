package net.travelsystem.reservationservice.exceptions;

public class ResourceAlreadyExists extends RuntimeException {
    public ResourceAlreadyExists(String message) {
        super(message);
    }
}
