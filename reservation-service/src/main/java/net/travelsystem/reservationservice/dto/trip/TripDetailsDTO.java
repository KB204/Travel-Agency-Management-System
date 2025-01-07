package net.travelsystem.reservationservice.dto.trip;

import java.time.Month;
import java.util.Map;

public record TripDetailsDTO(Map<Month,Long> monthCount, Map<Month,Double> monthTotal) {}
