package net.travelsystem.reservationservice.service;

import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import net.travelsystem.reservationservice.dao.ClientRepository;
import net.travelsystem.reservationservice.dao.ReservationRepository;
import net.travelsystem.reservationservice.dao.TripRepository;
import net.travelsystem.reservationservice.dto.reservation.ReservationRequest;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.entities.Client;
import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.entities.Trip;
import net.travelsystem.reservationservice.enums.ReservationStatus;
import net.travelsystem.reservationservice.exceptions.ReservationException;
import net.travelsystem.reservationservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.reservationservice.exceptions.ResourceNotFoundException;
import net.travelsystem.reservationservice.mapper.ReservationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.lang.String.format;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final TripRepository tripRepository;
    private final ClientRepository clientRepository;
    private final ReservationMapper mapper;

    private final static Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    public ReservationServiceImpl(ReservationRepository reservationRepository, TripRepository tripRepository, ClientRepository clientRepository, ReservationMapper mapper) {
        this.reservationRepository = reservationRepository;
        this.tripRepository = tripRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(mapper::reservationToDtoResponse)
                .toList();
    }

    @Override
    public void createNewReservation(Long tripId, ReservationRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Voyage n'existe pas"));

        if (request.nbrTickets() > trip.getAvailablePlaces())
            throw new ReservationException("Ce voyage n'a pas de places disponibles");

        Client client = mapper.requestDtoToClient(request);

        Reservation reservation = Reservation.builder()
                .identifier(UUID.randomUUID().toString().substring(0,10))
                .reservationDate(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .trip(trip)
                .client(client)
                .build();

        reservationRepository.findByIdentifierIgnoreCase(reservation.getIdentifier())
                .ifPresent(r -> {
                    throw new ResourceAlreadyExists("Reservation exists déja");
                });

        reservationRepository.save(reservation);
        clientRepository.save(client);
    }

    @Override
    @KafkaListener(topics = "${kafka.topic.payment.name}",groupId = "${spring.kafka.consumer.group-id}")
    public void completeReservation(PaymentEvent event) {
        Reservation reservation = reservationRepository.findByIdentifierIgnoreCase(event.reservationIdentifier())
                .orElseThrow(() -> new ResourceNotFoundException(format("La réservation numéro %s n'existe pas",event.reservationIdentifier())));

        Trip trip = tripRepository.findById(reservation.getTrip().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Voyage non trouvé"));

        reservation.setStatus(ReservationStatus.APPROVED);
        trip.setAvailablePlaces(trip.getAvailablePlaces() - reservation.getClient().getNbrTickets());

        reservationRepository.save(reservation);
        tripRepository.save(trip);
        logger.info("Consumed Payment Event");
    }

    @Override
    public Double reservationTotalAmount(String identifier) {
        Reservation reservation = reservationRepository.findByIdentifierIgnoreCase(identifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("La réservation numéro %s n'existe pas",identifier)));

        double tripPrice = reservation.getTrip().getPrice();
        int nbrTickets = reservation.getClient().getNbrTickets();

        return nbrTickets * tripPrice;
    }
}
