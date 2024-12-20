package net.travelsystem.reservationservice.service;

import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import net.travelsystem.reservationservice.clients.FlightConventionRest;
import net.travelsystem.reservationservice.clients.HotelConventionRest;
import net.travelsystem.reservationservice.dao.ClientRepository;
import net.travelsystem.reservationservice.dao.ReservationRepository;
import net.travelsystem.reservationservice.dao.TripRepository;
import net.travelsystem.reservationservice.dto.external_services.FlightConvention;
import net.travelsystem.reservationservice.dto.external_services.HotelConvention;
import net.travelsystem.reservationservice.dto.reservation.ReservationRequest;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.dto.reservation.UpdateReservationRequest;
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
    private final HotelConventionRest hotelConventionRest;
    private final FlightConventionRest flightConventionRest;
    private final NotificationService notificationService;
    private final ReservationMapper mapper;

    private final static Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    public ReservationServiceImpl(ReservationRepository reservationRepository, TripRepository tripRepository, ClientRepository clientRepository, HotelConventionRest hotelConventionRest, FlightConventionRest flightConventionRest, NotificationService notificationService, ReservationMapper mapper) {
        this.reservationRepository = reservationRepository;
        this.tripRepository = tripRepository;
        this.clientRepository = clientRepository;
        this.hotelConventionRest = hotelConventionRest;
        this.flightConventionRest = flightConventionRest;
        this.notificationService = notificationService;
        this.mapper = mapper;
    }

    @Override
    public List<ReservationResponse> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(reservation -> {
                    reservation.setHotelConvention(hotelConventionRest.findHotel(reservation.getTrip().getHotelConventionIdentifier()));
                    reservation.setFlightConvention(flightConventionRest.findFlight(reservation.getTrip().getFlightConventionIdentifier()));
                    return mapper.reservationToDtoResponse(reservation);
                })
                .toList();
    }

    @Override
    public void createNewReservation(Long tripId, ReservationRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Voyage n'existe pas"));

        HotelConvention convention = hotelConventionRest.findHotelConvention(trip.getHotelConventionIdentifier());
        FlightConvention convention2 = flightConventionRest.findFlightConvention(trip.getFlightConventionIdentifier());

        if (request.nbrTickets() > trip.getAvailablePlaces())
            throw new ReservationException("Ce voyage n'a pas de places disponibles");

        Client client = mapper.requestDtoToClient(request);

        Reservation reservation = Reservation.builder()
                .identifier(UUID.randomUUID().toString().substring(0,10))
                .reservationDate(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .hotelName(convention.hotel().name())
                .returnDate(convention.checkOutDate())
                .flightDepartureTime(convention2.flight().departureTime())
                .departureLocation(convention2.flight().origin())
                .airlineName(convention2.flight().airline().name())
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
        reservation.setTotalPrice(event.amount());
        trip.setAvailablePlaces(trip.getAvailablePlaces() - reservation.getClient().getNbrTickets());

        reservationRepository.save(reservation);
        tripRepository.save(trip);
        logger.info("Consumed Payment Event");
        notificationService.reservationNotification(reservation.getIdentifier());
    }

    @Override
    public void updateReservation(String identifier, UpdateReservationRequest request) {
        Reservation reservation = reservationRepository.findByIdentifierIgnoreCase(identifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("La réservation numéro %s n'existe pas",identifier)));

        reservation.setStatus(request.status());
        reservation.setUpdatedAt(LocalDateTime.now());

        reservationRepository.save(reservation);
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
