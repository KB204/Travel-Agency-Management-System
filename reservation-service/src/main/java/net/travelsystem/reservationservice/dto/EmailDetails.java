package net.travelsystem.reservationservice.dto;

import lombok.Builder;

@Builder
public record EmailDetails(String to,String subject,String body) {
}
