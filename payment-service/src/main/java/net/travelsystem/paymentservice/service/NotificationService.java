package net.travelsystem.paymentservice.service;

public interface NotificationService {
    void debitCardEvent(String reservationIdentifier,Double amount,String cardNumber);
}
