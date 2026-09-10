package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RankedRecord {
    private final Long id;
//    private final String submittedAt;
//    private final RankedClient client;
    private final RankedPlayer player;
    private final RankedRun run;
    private final RankedBossFight bossFight;
    private final RankedDeck deck;
}
