package net.travelsystem.reservationservice.service;

import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import net.travelsystem.reservationservice.clients.FlightConventionRest;
import net.travelsystem.reservationservice.clients.HotelConventionRest;
import net.travelsystem.reservationservice.dao.ClientRepository;
import net.travelsystem.reservationservice.dao.ReservationRepository;
import net.travelsystem.reservationservice.dao.TripRepository;
import net.travelsystem.reservationservice.dto.client.ClientResponseDetails;
import net.travelsystem.reservationservice.dto.external_services.FlightConvention;
import net.travelsystem.reservationservice.dto.external_services.HotelConvention;
import net.travelsystem.reservationservice.dto.reservation.ClientReservationRequest;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponse;
import net.travelsystem.reservationservice.dto.reservation.ReservationResponseDTO;
import net.travelsystem.reservationservice.dto.reservation.UpdateReservationRequest;
import net.travelsystem.reservationservice.entities.Client;
import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.entities.Trip;
import net.travelsystem.reservationservice.enums.ReservationStatus;
import net.travelsystem.reservationservice.exceptions.ReservationException;
import net.travelsystem.reservationservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.reservationservice.exceptions.ResourceNotFoundException;
import net.travelsystem.reservationservice.mapper.ClientMapper;
import net.travelsystem.reservationservice.mapper.ReservationMapper;
import net.travelsystem.reservationservice.service.specification.ReservationSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private final ClientMapper clientMapper;

    private final static Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);

    public ReservationServiceImpl(ReservationRepository reservationRepository, TripRepository tripRepository, ClientRepository clientRepository, HotelConventionRest hotelConventionRest, FlightConventionRest flightConventionRest, NotificationService notificationService, ReservationMapper mapper, ClientMapper clientMapper) {
        this.reservationRepository = reservationRepository;
        this.tripRepository = tripRepository;
        this.clientRepository = clientRepository;
        this.hotelConventionRest = hotelConventionRest;
        this.flightConventionRest = flightConventionRest;
        this.notificationService = notificationService;
        this.mapper = mapper;
        this.clientMapper = clientMapper;
    }

    @Override
    public Page<ReservationResponse> getAllReservations(String identifier, String status, Double amount, String date, String identity,
                                                        String lastname, String firstname, Pageable pageable) {

        Specification<Reservation> specification = ReservationSpecification.filterWithoutConditions()
                .and(ReservationSpecification.identifierEqual(identifier))
                .and(ReservationSpecification.statusEqual(status))
                .and(ReservationSpecification.amountEqual(amount))
                .and(ReservationSpecification.reservationDateLike(date))
                .and(ReservationSpecification.clientIdentityEqual(identity))
                .and(ReservationSpecification.clientFirstNameLike(firstname))
                .and(ReservationSpecification.clientLastNameLike(lastname));
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), Sort.by("reservationDate").descending());

        return reservationRepository.findAll(specification,pageable)
                .map(reservation -> {
                    reservation.setHotelConvention(hotelConventionRest.findHotel(reservation.getTrip().getHotelConventionIdentifier()));
                    reservation.setFlightConvention(flightConventionRest.findFlight(reservation.getTrip().getFlightConventionIdentifier()));
                    return mapper.reservationToDtoResponse(reservation);
                });
    }

    @Override
    public void createNewReservation(Long tripId, ClientReservationRequest request) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Voyage n'existe pas"));

        HotelConvention convention = hotelConventionRest.getHotelConventionDetails(trip.getHotelConventionIdentifier());
        FlightConvention convention2 = flightConventionRest.getFlightConventionDetails(trip.getFlightConventionIdentifier());

        if (request.nbrTickets() > trip.getAvailablePlaces())
            throw new ReservationException("Ce voyage n'a pas de places disponibles");

        Client client = clientMapper.requestDtoToClient(request);

        Reservation reservation = Reservation.builder()
                .identifier(UUID.randomUUID().toString().substring(0,10))
                .reservationDate(LocalDateTime.now())
                .status(ReservationStatus.PENDING)
                .nbrTickets(request.nbrTickets())
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

        Double total = reservationTotalAmount(reservation);
        reservation.setTotalPrice(total);

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
        reservation.setUpdatedAt(LocalDateTime.now());
        reservation.setTotalPrice(event.amount());
        trip.setAvailablePlaces(trip.getAvailablePlaces() - reservation.getNbrTickets());

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
    public ClientResponseDetails getClientReservations(String identity, String status, Double amount, String date, Pageable pageable) {
        Client client = clientRepository.findByIdentity(identity)
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(format("Client identifié par %s n'existe pas",identity)));

        Specification<Reservation> specification = Specification.where(ReservationSpecification.clientIdentityEqual(client.getIdentity()))
                .and(ReservationSpecification.statusEqual(status))
                .and(ReservationSpecification.amountEqual(amount))
                .and(ReservationSpecification.reservationDateLike(date));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("reservationDate").descending());

        Page<Reservation> reservations = reservationRepository.findAll(specification,pageable);
        List<ReservationResponseDTO> responseDTO = reservations
                .stream()
                .map(mapper::reservationToDtoDetails)
                .toList();

        return new ClientResponseDetails(client.getIdentity(), client.getFirstName(), client.getLastName(), responseDTO);
    }

    @Override
    public Double reservationTotalAmount(String identifier) {
        Reservation reservation = reservationRepository.findByIdentifierIgnoreCase(identifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("La réservation numéro %s n'existe pas",identifier)));

        double tripPrice = reservation.getTrip().getPrice();
        int nbrTickets = reservation.getNbrTickets();

        return nbrTickets * tripPrice;
    }

    @Override
    public void deleteReservation(String identifier) {
        Reservation reservation = reservationRepository.findByIdentifierIgnoreCase(identifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("La réservation numéro %s n'existe pas",identifier)));

        reservationRepository.delete(reservation);
    }

    private Double reservationTotalAmount(Reservation reservation) {
        double tripPrice = reservation.getTrip().getPrice();
        int nbrTickets = reservation.getNbrTickets();

        return nbrTickets * tripPrice;
    }
}
