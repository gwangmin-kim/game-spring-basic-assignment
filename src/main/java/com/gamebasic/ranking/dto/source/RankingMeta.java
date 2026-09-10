package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RankingMeta {
    private final RankSeason season;
    private final String generatedAt;
    private final int schemaVersion;
    private final int totalRecords;
}
