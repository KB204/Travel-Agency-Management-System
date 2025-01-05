package net.travelsystem.reservationservice.batch;

import lombok.extern.slf4j.Slf4j;
import net.travelsystem.reservationservice.entities.Reservation;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ReservationProcessor implements ItemProcessor<Reservation,Reservation> {
    @Override
    public Reservation process(Reservation item) throws Exception {
        log.info("Processing The Item : {}",item);
        if (item.getReservationDate().isBefore(LocalDateTime.now().minusHours(48))){
            return item;
        }
        return null;
    }
}
