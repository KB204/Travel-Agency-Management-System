package net.travelsystem.reservationservice.service;

import net.travelsystem.reservationservice.dao.ReservationRepository;
import net.travelsystem.reservationservice.dto.EmailDetails;
import net.travelsystem.reservationservice.entities.Reservation;
import net.travelsystem.reservationservice.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.String.format;

@Service
public class EmailNotificationService implements NotificationService {
    private final ReservationRepository repository;
    private final EmailService emailService;

    public EmailNotificationService(ReservationRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Override
    public void reservationNotification(String identifier) {
        Reservation reservation = repository.findByIdentifierIgnoreCase(identifier)
                .orElseThrow(() -> new ResourceNotFoundException(format("Reservation numéro %s n'existe pas",identifier)));

        String customerEmail = reservation.getClient().getEmail();
        String customerFirstName = reservation.getClient().getFirstName();
        String customerLastName = reservation.getClient().getLastName();
        String tripDestination = reservation.getTrip().getDestination();
        int nbrTravelers = reservation.getTrip().getAvailablePlaces();
        double totalAmount = reservation.getTotalPrice();

        LocalDateTime departureTime = reservation.getFlightDepartureTime();
        String departureOrigin = reservation.getDepartureLocation();
        LocalDate returnDate = reservation.getReturnDate();
        String hotelName = reservation.getHotelName();
        String airlineName = reservation.getAirlineName();

        EmailDetails emailDetails = EmailDetails.builder()
                .to(customerEmail)
                .body(format("""
                        Cher(e) %s,
                                                
                        Nous vous remercions d'avoir choisi notre agence de voyage pour vos prochaines vacances. Nous avons le plaisir de vous confirmer que votre réservation a été effectuée avec succès.
                                                
                        Voici les détails de votre réservation :
                                                
                        Réservation N° : %s
                        Nom du voyageur : %s %s
                        Destination : %s
                        Nombre de voyageurs : %s
                        Date de départ : %s
                        Lieu de départ : %s
                        Date de retour : %s
                        Hébergement : %s
                        Compagnie aérienne : %s
                        Total payé : %sMAD
                        Mode de paiement : Carte bancaire
                        
                        Pour toute question ou demande d'assistance, n'hésitez pas à nous contacter par email ou par téléphone.
                                                
                        Nous vous remercions encore pour votre confiance et vous souhaitons un excellent voyage !
                                                
                        Cordialement.
                        """,customerFirstName,identifier,customerLastName,customerFirstName,tripDestination,nbrTravelers,departureTime,departureOrigin,returnDate,hotelName,airlineName,totalAmount))
                .subject("Confirmation de votre réservation – [Agence de voyage x]")
                .build();

        emailService.sendEmail(emailDetails);
    }
}
