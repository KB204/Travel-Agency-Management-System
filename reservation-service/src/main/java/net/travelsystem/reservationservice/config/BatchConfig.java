package net.travelsystem.reservationservice.config;

import net.travelsystem.reservationservice.batch.ReservationProcessor;
import net.travelsystem.reservationservice.dao.ReservationRepository;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.enums.ReservationStatus;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

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

    @Bean
    public FlatFileItemWriter<ReservationResponse> flatFileItemReader() {
        return new FlatFileItemWriterBuilder<ReservationResponse>()
                .name("Fichier des réservations en cours de traitement")
                .resource(new FileSystemResource(""))
                .headerCallback(writer -> writer.append("En tête du fichier"))
                .delimited()
                .delimiter(";")
                .sourceType(ReservationResponse.class)
                .names("")
                .shouldDeleteIfEmpty(Boolean.TRUE)
                .append(Boolean.FALSE)
                .build();
    }
}
