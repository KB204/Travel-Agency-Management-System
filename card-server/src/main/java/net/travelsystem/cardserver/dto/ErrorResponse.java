package net.travelsystem.cardserver.dto;

import java.util.List;

public record ErrorResponse(String message, List<String> details) {
}
