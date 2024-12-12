package net.travelsystem.cardserver.mapper;

import net.travelsystem.cardserver.dto.CardRequest;
import net.travelsystem.cardserver.dto.CardResponse;
import net.travelsystem.cardserver.entity.Card;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardMapper {
    CardResponse cardToDtoResponse(Card card);
    Card requestDtoToCard(CardRequest request);
}
