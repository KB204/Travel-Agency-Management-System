package net.travelsystem.paymentservice.controller;

import feign.FeignException;
import net.travelsystem.paymentservice.dto.ErrorResponse;
import net.travelsystem.paymentservice.dto.ResourceAlreadyExists;
import net.travelsystem.paymentservice.exceptions.PaymentException;
import net.travelsystem.paymentservice.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorResponse error = new ErrorResponse("Validation échouée", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Erreurs fonctionnelles", details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExists.class)
    final ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExists ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Erreurs fonctionnelles", details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentException.class)
    final ResponseEntity<Object> handlePaymentException(PaymentException ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getLocalizedMessage());
        ErrorResponse error = new ErrorResponse("Erreurs fonctionnelles",details);
        return new ResponseEntity<>(error,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FeignException.class)
    final ResponseEntity<Object> handleFeignException() {
        List<String> details = new ArrayList<>();
        details.add("Impossible de se connecter au service. Veuillez réessayer plus tard");
        ErrorResponse error = new ErrorResponse("Erreurs techniques", details);
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    final ResponseEntity<String> handleRestClientNotFoundException(HttpClientErrorException ex) {
        return new ResponseEntity<>(ex.getLocalizedMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAccessException.class)
    final ResponseEntity<Object> handleRestClientOutOfService() {
        List<String> details = new ArrayList<>();
        details.add("Impossible de se connecter au service. Veuillez réessayer plus tard");
        ErrorResponse error = new ErrorResponse("Erreurs techniques", details);
        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(Exception.class)
    final ResponseEntity<Object> handleOtherExceptions(Exception ex) {
        List<String> details = new ArrayList<>();
        details.add(ex.getMessage());
        ErrorResponse error = new ErrorResponse("Erreurs techniques", details);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
