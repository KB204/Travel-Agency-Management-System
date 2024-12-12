package net.travelsystem.paymentservice.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        return switch (response.status()){
            case 404 -> new ResourceNotFoundException("voyage non trouvé");
            default -> new Exception("Un Problème est servenu réessayer plus tard");
        };
    }
}
