package net.travelsystem.paymentservice.mapper;

import net.travelsystem.paymentservice.dto.refund.RefundResponse;
import net.travelsystem.paymentservice.entities.Refund;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RefundMapper {
    RefundResponse refundToDtoResponse(Refund refund);
}
