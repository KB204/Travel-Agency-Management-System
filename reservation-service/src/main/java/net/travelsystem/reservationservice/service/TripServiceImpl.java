package net.travelsystem.reservationservice.service;

import net.travelsystem.reservationservice.clients.FlightConventionRest;
import net.travelsystem.reservationservice.clients.HotelConventionRest;
import net.travelsystem.reservationservice.dao.TripRepository;
import net.travelsystem.reservationservice.dto.external_services.FlightConvention;
import net.travelsystem.reservationservice.dto.external_services.HotelConvention;
import net.travelsystem.reservationservice.dto.trip.TripDetailsDTO;
import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripResponse;
import net.travelsystem.reservationservice.dto.trip.TripUpdateRequest;
import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.entities.Trip;
import net.travelsystem.reservationservice.enums.ReservationStatus;
import net.travelsystem.reservationservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.reservationservice.exceptions.ResourceNotFoundException;
import net.travelsystem.reservationservice.mapper.TripMapper;
import net.travelsystem.reservationservice.service.specification.TripSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;
    private final HotelConventionRest rest;
    private final FlightConventionRest flightRest;
    private final TripMapper mapper;

    public TripServiceImpl(TripRepository tripRepository, HotelConventionRest rest, FlightConventionRest flightRest, TripMapper mapper) {
        this.tripRepository = tripRepository;
        this.rest = rest;
        this.flightRest = flightRest;
        this.mapper = mapper;
    }

    @Override
    public Page<TripResponse> getAllTrips(String name, Double price, String destination, String identifier, Pageable pageable) {

        Specification<Trip> specification = TripSpecification.filterWithoutConditions()
                .and(TripSpecification.nameLike(name))
                .and(TripSpecification.priceEqual(price))
                .and(TripSpecification.destinationLike(destination))
                .and(TripSpecification.hotelConventionEqual(identifier));
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("price").descending());

        return tripRepository.findAll(specification,pageable)
                .map(trip -> {
                    trip.setHotelConvention(rest.findHotelConvention(trip.getHotelConventionIdentifier()));
                    trip.setFlightConvention(flightRest.findFlightConvention(trip.getFlightConventionIdentifier()));
                    return mapper.tripToDtoResponse(trip);
                });
    }

    @Override
    public void createTrip(TripRequest request) {
        HotelConvention hotelConvention = rest.getHotelConventionDetails(request.hotelConventionIdentifier());
        FlightConvention flightConvention = flightRest.getFlightConventionDetails(request.flightConventionIdentifier());

        tripRepository.findByHotelConventionIdentifierOrFlightConventionIdentifier(request.hotelConventionIdentifier(), request.flightConventionIdentifier())
                .ifPresent(trip -> {
                    throw new ResourceAlreadyExists("Convention avec hotel ou compagnie aérienne exists déja");
                });

        Trip trip = mapper.dtoRequestToTrip(request);
        trip.setHotelConventionIdentifier(hotelConvention.identifier());
        trip.setFlightConventionIdentifier(flightConvention.flight().flightNo());
        tripRepository.save(trip);
    }

    @Override
    public void updateTrip(Long id, TripUpdateRequest request) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voyage non trouvé"));

        trip.setName(request.name());
        trip.setDescription(request.description());
        trip.setPrice(request.price());
        trip.setOrigin(request.origin());
        trip.setDestination(request.destination());
        trip.setDuration(request.duration());
        trip.setAvailablePlaces(request.availablePlaces());

        tripRepository.save(trip);
    }

    @Override
    @Transactional
    public TripDetailsDTO tripReservationsDetails() {
        List<Trip> trips = tripRepository.findAll();

        Map<Map.Entry<Integer,Month>, Long> monthYearCount = trips.stream()
                .flatMap(trip -> trip.getReservations().stream())
                .filter(reservation -> reservation.getStatus().equals(ReservationStatus.APPROVED))
                .collect(Collectors.groupingBy(
                        reservation -> Map.entry(
                                reservation.getReservationDate().getYear(),
                                reservation.getReservationDate().getMonth()
                        ),
                        Collectors.counting()));

        Map<Map.Entry<Integer,Month>, Double> monthYearTotal = trips.stream()
                .flatMap(trip -> trip.getReservations().stream())
                .filter(reservation -> reservation.getStatus().equals(ReservationStatus.APPROVED))
                .collect(Collectors.groupingBy(
                        reservation -> Map.entry(
                                reservation.getReservationDate().getYear(),
                                reservation.getReservationDate().getMonth()
                        ),
                        Collectors.summingDouble(Reservation::getTotalPrice)));

        Map<Integer,Double> totalYear = trips.stream()
                .flatMap(trip -> trip.getReservations().stream())
                .filter(reservation -> reservation.getStatus().equals(ReservationStatus.APPROVED))
                .collect(Collectors.groupingBy(
                        reservation -> reservation.getReservationDate().getYear(),
                        Collectors.summingDouble(Reservation::getTotalPrice)
                ));

        Map<Map.Entry<Integer, Month>, Double> monthPercentage = monthYearTotal.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            Integer year = entry.getKey().getKey();
                            Double monthTotalValue = entry.getValue();
                            Double yearTotalValue = totalYear.get(year);
                            return (monthTotalValue / yearTotalValue) * 100;
                        }
                ));

        return new TripDetailsDTO(monthYearCount,monthYearTotal,monthPercentage,totalYear);
    }

    @Override
    public void deleteTrip(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Voyage non trouvé"));

        tripRepository.delete(trip);
    }
}
