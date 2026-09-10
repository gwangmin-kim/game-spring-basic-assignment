package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RankedDeck {
    private final int size;
    private final List<RankedCard> cards;
}
