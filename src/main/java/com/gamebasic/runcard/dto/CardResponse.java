package com.gamebasic.runcard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CardResponse {
    private final Long id;
    private final String cardType;
    private final int acquiredFloor;
}
