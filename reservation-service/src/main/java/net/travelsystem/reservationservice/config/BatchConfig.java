package net.travelsystem.reservationservice.config;

import net.travelsystem.reservationservice.batch.ReservationProcessor;
import net.travelsystem.reservationservice.dao.ReservationRepository;
import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.enums.ReservationStatus;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private final ReservationRepository reservationRepository;
    private final ReservationProcessor processor;

    public BatchConfig(ReservationRepository reservationRepository, ReservationProcessor processor) {
        this.reservationRepository = reservationRepository;
        this.processor = processor;
    }

    @Bean
    public ListItemReader<Reservation> pendingReservationsProcessing() {
        var reservations = reservationRepository.findByStatus(ReservationStatus.PENDING);
        return new ListItemReader<>(reservations);
    }
}
