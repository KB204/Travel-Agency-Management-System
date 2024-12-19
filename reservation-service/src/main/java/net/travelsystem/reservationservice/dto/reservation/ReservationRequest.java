package net.travelsystem.reservationservice.dto.reservation;

import jakarta.validation.constraints.*;

public record ReservationRequest(
        @NotEmpty(message = "La carte d'identité du client est obligatoire")
        String identity,
        @NotEmpty(message = "Le numéro de passport du client est obligatoire")
        String passportNumber,
        @NotEmpty(message = "Le prénom du client est obligatoire")
        String firstName,
        @NotEmpty(message = "Le nom du client est obligatoire")
        String lastName,
        @NotEmpty(message = "L'email du client est obligatoire")
        @Email(message = "Veuillez entrer un email valide")
        String email,
        @NotEmpty(message = "Le numéro de teléphone du client est obligatoire")
        @Size(min = 10,max = 10,message = "Veuillez entrer un numéro de teléphone valide")
        String phoneNumber,
        @NotNull(message = "Veuillez sélectionner le nombre de places")
        @Min(value = 1,message = "Il faut sélectionner au moins un tickets")
        @Max(value = 3,message = "Vous pouvez pas sélectionner plus que 3 tickets")
        Integer nbrTickets) {}
