package net.travelsystem.paymentservice.mapper;

import net.travelsystem.paymentservice.dto.payment.PaymentResponse;
import net.travelsystem.paymentservice.entities.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse paymentToDtoResponse(Payment payment);
}
