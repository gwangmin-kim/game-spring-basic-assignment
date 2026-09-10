package com.gamebasic.runcard.dto;

import com.gamebasic.runcard.entity.RunCard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CardResponse {
    private final Long id;
    private final String cardType;
    private final int acquiredFloor;

    public static CardResponse from(RunCard card) {
        return new CardResponse(card.getId(), card.getCardType(), card.getAcquiredFloor());
    }
}
