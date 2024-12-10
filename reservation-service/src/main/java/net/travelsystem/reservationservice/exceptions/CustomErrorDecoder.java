package net.travelsystem.reservationservice.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()){
            case 404 -> new ResourceNotFoundException("Convention avec hotel non trouvé");
            case 400 -> new ResourceNotFoundException("Convention avec Compagnie aérienne non trouvé");
            default -> new Exception("Un Problème est servenu réessayer plus tard");
        };
    }
}
