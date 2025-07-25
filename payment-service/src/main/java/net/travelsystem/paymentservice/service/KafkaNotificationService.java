package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class KafkaNotificationService implements NotificationService {
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Value("${kafka.topic.payment.name}")
    private String paymentTopic;
    private final static Logger logger = LoggerFactory.getLogger(KafkaNotificationService.class);

    public KafkaNotificationService(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void debitCardEvent(String reservationIdentifier,Double amount, String cardNumber) {
        logger.info("Start - Sending Payment Event");

        kafkaTemplate.send(paymentTopic, new PaymentEvent(reservationIdentifier, amount, cardNumber));

        logger.info("End - Sending Payment Event");
    }
}
