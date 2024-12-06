package net.travelsystem.reservationservice.service;

import net.travelsystem.reservationservice.clients.HotelConventionRest;
import net.travelsystem.reservationservice.dao.TripRepository;
import net.travelsystem.reservationservice.dto.external_services.HotelConvention;
import net.travelsystem.reservationservice.dto.trip.TripRequest;
import net.travelsystem.reservationservice.dto.trip.TripResponse;
import net.travelsystem.reservationservice.entities.Trip;
import net.travelsystem.reservationservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.reservationservice.mapper.TripMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripServiceImpl implements TripService {
    private final TripRepository tripRepository;
    private final HotelConventionRest rest;
    private final TripMapper mapper;

    public TripServiceImpl(TripRepository tripRepository, HotelConventionRest rest, TripMapper mapper) {
        this.tripRepository = tripRepository;
        this.rest = rest;
        this.mapper = mapper;
    }

    @Override
    public List<TripResponse> getAllTrips() {
        return tripRepository.findAll()
                .stream()
                .map(trip -> {
                    trip.setHotel(rest.findHotelConvention(trip.getHotelConventionIdentifier()));
                    return mapper.tripToDtoResponse(trip);
                })
                .toList();
    }

    @Override
    public void createTrip(TripRequest request) {
        HotelConvention hotelConvention = rest.getHotelConventionDetails(request.hotelConvention());

        tripRepository.findByHotelConventionIdentifierOrFlightConventionIdentifier(request.hotelConvention(), request.flightConvention())
                .ifPresent(trip -> {
                    throw new ResourceAlreadyExists("Convention avec hotel ou compagnie aérienne exists déja");
                });

        Trip trip = mapper.dtoRequestToTrip(request);
        trip.setHotelConventionIdentifier(hotelConvention.identifier());
        tripRepository.save(trip);
    }

    @Override
    public void updateTrip(TripRequest tripRequest) {

    }

    @Override
    public void deleteTrip(Long id) {

    }
}
