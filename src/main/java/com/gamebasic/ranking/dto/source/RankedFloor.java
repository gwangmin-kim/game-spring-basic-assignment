package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RankedFloor {
    private final int floor;
    private final String enemy;
    private final int turns;
    private final int hpAfter;
    private final List<RankedFloorReward> rewards;
}
