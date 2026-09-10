package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RankedClient {
    private final String version;
    private final String platform;
    private final String locale;
}
