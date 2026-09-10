package com.gamebasic.ranking.dto.source;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class RankedPlayer {
    private final String id;
    private final String name;
//    private final String region;
//    private final List<String> tags;
}
