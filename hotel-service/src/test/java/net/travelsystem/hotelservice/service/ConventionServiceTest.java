package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dao.ConventionRepository;
import net.travelsystem.hotelservice.dao.HotelRepository;
import net.travelsystem.hotelservice.dto.convention.ConventionRequest;
import net.travelsystem.hotelservice.entities.Convention;
import net.travelsystem.hotelservice.entities.Hotel;
import net.travelsystem.hotelservice.entities.Room;
import net.travelsystem.hotelservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.hotelservice.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConventionServiceTest {
    @Mock
    private ConventionRepository conventionRepository;
    @Mock
    private HotelRepository hotelRepository;
    @InjectMocks
    private ConventionServiceImpl underTest;

    @Test
    void shouldCreateNewHotelConventionWhenHotelExists() {
        // given
        List<Room> rooms = new ArrayList<>();
        Hotel hotel = new Hotel(1L,"test","test","test",rooms);
        ConventionRequest request = new ConventionRequest(hotel.getName(), hotel.getLocation(), LocalDate.now(), LocalDate.of(2024,12,12));

        // when
        when(hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(request.name(),request.location())).thenReturn(Optional.of(hotel));
        when(conventionRepository.findByIdentifierIgnoreCase(anyString())).thenReturn(Optional.empty());
        underTest.createHotelConvention(request);

        // then
        ArgumentCaptor<Convention> captor = ArgumentCaptor.forClass(Convention.class);
        verify(conventionRepository).save(captor.capture());
        Convention savedConvention = captor.getValue();

        assertThat(savedConvention).isNotNull();
        assertThat(savedConvention.getHotel().getName()).isEqualTo(request.name());
        assertThat(savedConvention.getHotel().getLocation()).isEqualTo(request.location());
        assertThat(savedConvention.getCheckInDate()).isEqualTo(request.checkInDate());
        assertThat(savedConvention.getCheckOutDate()).isEqualTo(request.checkOutDate());
    }
    @Test
    void shouldNotCreateNewHotelConventionWhenHotelNotFound() {
        // given
        ConventionRequest request = new ConventionRequest("test", "test", LocalDate.now(), LocalDate.of(2024,12,12));

        // when
        when(hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(request.name(),request.location())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> underTest.createHotelConvention(request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(conventionRepository,never()).save(any(Convention.class));
    }
    @Test
    void shouldNotCreateNewHotelConventionWhenIdentifierAlreadyExits() {
        // given
        List<Room> rooms = new ArrayList<>();
        Hotel hotel = new Hotel(1L,"test","test","test",rooms);
        ConventionRequest request = new ConventionRequest("test", "test", LocalDate.now(), LocalDate.of(2024,12,12));
        Convention convention = new Convention();

        // when
        when(hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(request.name(),request.location())).thenReturn(Optional.of(hotel));
        when(conventionRepository.findByIdentifierIgnoreCase(anyString())).thenReturn(Optional.of(convention));

        // then
        assertThatThrownBy(() -> underTest.createHotelConvention(request))
                .isInstanceOf(ResourceAlreadyExists.class);
        verify(conventionRepository,never()).save(any(Convention.class));
    }
}