package net.travelsystem.reservationservice.batch;

import lombok.extern.slf4j.Slf4j;
import net.travelsystem.reservationservice.entities.Reservation;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class ReservationProcessor implements ItemProcessor<Reservation,Reservation> {
    @Override
    public Reservation process(Reservation item) throws Exception {
        log.info("Processing The Item : {}",item);
        if (item.getReservationDate().isBefore(LocalDateTime.now())){
            //item.setReservationDate(LocalDateTime.parse(item.getReservationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            return item;
        }
        return null;
    }
}
