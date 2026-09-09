package com.gamebasic.runcard.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class DeckCount {
    private final Long game_id;
    private final int deck_size;

    public DeckCount(Long game_id, Long deck_size) {
        this.game_id = game_id;
        this.deck_size = deck_size.intValue();
    }
}
