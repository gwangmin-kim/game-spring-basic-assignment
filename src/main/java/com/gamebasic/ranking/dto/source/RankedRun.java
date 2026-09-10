package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RankedRun {
//    private final String seed;
    private final String status;
    private final int clearedFloor;
    private final int durationSeconds;
    private final int finalHp;
//    private final List<RankedFloor> floors;
}
