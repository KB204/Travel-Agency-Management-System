package net.travelsystem.hotelservice.dto;

import java.util.List;

public record ErrorResponse(String message, List<String> details) {
}
