package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RankSeason {
    private final String id;
    private final String name;
    private final String startsAt;
    private final String endsAt;
}
