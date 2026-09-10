package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RankedBossPhase {
    private final String phase;
    private final int turns;
    private final int damageTaken;
}
