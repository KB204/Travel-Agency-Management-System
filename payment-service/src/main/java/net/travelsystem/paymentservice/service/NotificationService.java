package net.travelsystem.paymentservice.service;

public interface NotificationService {
    void debitCardEvent(Long TripId,Double amount,String cardNumber);
}
