package net.travelsystem.reservationservice.dto.trip;

import java.time.Month;
import java.util.Map;

public record TripDetailsDTO(
        Map<Map.Entry<Integer,Month>, Long> monthYearCount,
        Map<Map.Entry<Integer,Month>, Double> monthYearTotal,
        Map<Map.Entry<Integer,Month>, Double> monthPercentage,
        Map<Integer,Double> yearTotal) {}
