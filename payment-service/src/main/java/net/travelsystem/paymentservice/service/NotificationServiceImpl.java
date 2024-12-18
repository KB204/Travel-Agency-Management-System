package net.travelsystem.paymentservice.service;

import net.travelsystem.paymentservice.dto.event.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
public class NotificationServiceImpl implements NotificationService {
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Value("${kafka.topic.payment.name}")
    private String paymentTopic;
    private final static Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    public NotificationServiceImpl(KafkaTemplate<String, PaymentEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void debitCardEvent(Long tripId,Double amount, String cardNumber) {
        logger.info("Start - Sending Payment Event");

        kafkaTemplate.send(paymentTopic, new PaymentEvent(tripId, amount, cardNumber));

        logger.info("End - Sending Payment Event");
    }
}
