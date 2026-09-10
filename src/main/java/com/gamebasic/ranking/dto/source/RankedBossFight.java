package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RankedBossFight {
    private final List<RankedBossPhase> phases;
    private final String finishingCard;
    private final int totalTurns;
}
