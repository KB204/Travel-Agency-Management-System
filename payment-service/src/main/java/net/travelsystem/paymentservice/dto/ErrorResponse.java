package net.travelsystem.paymentservice.dto;

import java.util.List;

public record ErrorResponse(String message, List<String> details) {
}
