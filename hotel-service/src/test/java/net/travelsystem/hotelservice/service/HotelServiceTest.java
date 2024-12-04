package net.travelsystem.hotelservice.service;

import net.travelsystem.hotelservice.dao.HotelRepository;
import net.travelsystem.hotelservice.dto.hotel.HotelRequest;
import net.travelsystem.hotelservice.entities.Hotel;
import net.travelsystem.hotelservice.entities.Room;
import net.travelsystem.hotelservice.exceptions.ResourceAlreadyExists;
import net.travelsystem.hotelservice.exceptions.ResourceNotFoundException;
import net.travelsystem.hotelservice.mapper.HotelMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceTest {
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private HotelMapper mapper;
    @InjectMocks
    private HotelServiceImpl service;

    @Test
    void shouldCreateNewHotelWhenNameAndLocationIsUnique() {
        // given
        List<Room> rooms = new ArrayList<>();
        Hotel hotel = new Hotel(1L,"test","test","test",rooms);
        HotelRequest request = new HotelRequest("test","test","test");

        // when
        when(mapper.dtoRequestToHotel(request)).thenReturn(hotel);
        when(hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(request.name(),request.location())).thenReturn(Optional.empty());

        service.createNewHotel(request);

        // then
        verify(hotelRepository).save(hotel);
        assertThat(hotel).isNotNull();
        assertThat(hotel.getName()).isEqualTo(request.name());
        assertThat(hotel.getLocation()).isEqualTo(request.location());
        assertThat(hotel.getContact()).isEqualTo(request.contact());
    }
    @Test
    void shouldNotCreateNewHotel(){
        // given
        List<Room> rooms = new ArrayList<>();
        Hotel hotel = new Hotel(1L,"test","test","test",rooms);
        HotelRequest request = new HotelRequest("test","test","test");

        // when
        when(hotelRepository.findByNameIgnoreCaseAndLocationIgnoreCase(request.name(), request.location())).thenReturn(Optional.of(hotel));

        // then
        assertThatThrownBy(() -> service.createNewHotel(request))
                .isInstanceOf(ResourceAlreadyExists.class);
        verify(hotelRepository,never()).save(hotel);
    }

    @Test
    void shouldUpdateExistingHotel() {
        // given
        List<Room> rooms = new ArrayList<>();
        Hotel hotel = new Hotel(1L,"test","test","test",rooms);
        HotelRequest request = new HotelRequest("test","test","test");

        // when
        when(hotelRepository.findById(1L)).thenReturn(Optional.of(hotel));
        service.updateHotel(hotel.getId(), request);

        // then
        verify(hotelRepository).save(hotel);
        assertThat(hotel).isNotNull();
        assertThat(hotel.getName()).isEqualTo(request.name());
        assertThat(hotel.getLocation()).isEqualTo(request.location());
        assertThat(hotel.getContact()).isEqualTo(request.contact());
    }
    @Test
    void shouldNotUpdateHotel(){
        // given
        List<Room> rooms = new ArrayList<>();
        Hotel hotel = new Hotel(10L,"test","test","test",rooms);
        HotelRequest request = new HotelRequest("test","test","test");

        // when
        when(hotelRepository.findById(hotel.getId())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> service.updateHotel(hotel.getId(), request))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(hotelRepository,never()).save(hotel);
    }

}