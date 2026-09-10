package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RankingSource {
    private final RankingMeta meta;
    private final List<RankedRecord> records;
}
