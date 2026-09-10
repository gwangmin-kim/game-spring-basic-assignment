package com.gamebasic.game.dto;

import com.gamebasic.game.entity.Game;
import com.gamebasic.game.entity.GamePhase;
import com.gamebasic.game.entity.GameStatus;
import com.gamebasic.runcard.dto.CardResponse;
import com.gamebasic.runcard.entity.RunCard;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class GameDetailResponse {
    private final Long id;
    private final String playerName;
    private final int currentHp;
    private final int currentFloor;
    private final GamePhase phase;
    private final GameStatus status;
    private final List<CardResponse> deck;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public static GameDetailResponse from(Game game, List<RunCard> cards) {
        List<CardResponse> deck = cards.stream().map(CardResponse::from).toList();
        return new GameDetailResponse(
                game.getId(),
                game.getPlayerName(),
                game.getCurrentHp(),
                game.getCurrentFloor(),
                game.getPhase(),
                game.getStatus(),
                deck,
                game.getCreatedAt(),
                game.getUpdatedAt()
        );
    }
}
