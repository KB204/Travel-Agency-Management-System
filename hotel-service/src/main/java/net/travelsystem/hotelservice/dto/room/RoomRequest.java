package net.travelsystem.hotelservice.dto.room;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import net.travelsystem.hotelservice.enums.RoomType;

public record RoomRequest(
        @NotEmpty(message = "L'hotel est obligatoire")
        String name,
        @NotEmpty(message = "La localisation de l'hotel est obligatoire")
        String location,
        @NotEmpty(message = "Le numéro de la chambre est obligatoire")
        String roomNumber,
        @NotNull(message = "Le type de la chambre est obligatoire")
        RoomType roomType,
        @NotNull(message = "Le prix de la nuitée est obligtoire")
        @Min(value = 100,message = "Le prix ne peut pas être inférieur a 100")
        Double price,
        @NotNull(message = "La disponiblité de la chambre est obligatoire")
        Boolean available) {}
