package com.gamebasic.ranking.service;

import com.gamebasic.ranking.dto.RankingEntry;
import com.gamebasic.ranking.dto.RankingResponse;
import com.gamebasic.ranking.dto.source.*;
import com.gamebasic.ranking.repository.RankingClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RankingService {
    private final RankingClient rankingClient;

    public RankingResponse getRankings() {
        // 데이터 가져오기
        RankingSource source;
        try {
             source = rankingClient.fetch();
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        if (source == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 레코드 가져오기
        List<RankedRecord> records = source.getRecords();
        if (records == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);

        // 메타데이터 추출
        RankingMeta meta = source.getMeta();
        RankSeason season = meta != null ? meta.getSeason() : null;
        String seasonId = season != null ? season.getId() : null;
        if (seasonId == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        // int totalRecords = meta != null ? meta.getTotalRecords() : records.size();
        // meta에 잘못된 값이 들어있을수 있음을 전제
        // 과제 문서에도 meta의 필드 값이 아닌 'record의 개수'라고 명시함
        // 실제 레코드 개수를 세는 것으로 대체
        int totalRecords = records.size();

        // 유효한 레코드 선별하기
        List<RankedRecord> validRecords = new ArrayList<>();
        int excludedCount = 0;

        for (RankedRecord record : records) {
            RankedRun run = record.getRun();
            // 먼저 최소 조건 검사 (클리어했는지)
            // 여기서 탈락하면 excludedCount는 증가하지 않음
            if (run == null
                ||!Objects.equals(run.getStatus(), "CLEARED")
                || run.getClearedFloor() != 10)
                continue;

            // 그 다음에 정상 응답 검증
            if (!RankingRecordValidator.checkRecordValidity(record)) {
                excludedCount++;
                continue;
            }

            validRecords.add(record);
        }

        validRecords.sort(RANKING_ORDER);

        // 플레이어 당 최고 기록 하나만 남기기
        // 응답 DTO도 구축
        // 중복 검사용 set
        Set<String> includedPlayers = new HashSet<>();
        // 랭크 기록용 int
        int rank = 0;
        List<RankingEntry> entries = new ArrayList<>();
        for (RankedRecord record : validRecords) {
            // null 방지를 위해 체이닝 없이 한 단계씩 접근
            RankedPlayer player = record.getPlayer();
            if (player == null) continue;

            var id = player.getId();
            if (id == null) continue;

            // 이미 확인된 플레이어라면 무시한다
            // 우선순위가 높은 순으로 정렬되어있으니
            // 나중에 발견한 건 전부 제거하면 된다
            if (includedPlayers.contains(id)) continue;

            includedPlayers.add(id);
            rank++;

            RankedRun run = record.getRun();
            RankedBossFight bossFight = record.getBossFight();
            RankedDeck deck = record.getDeck();

            entries.add(new RankingEntry(
                    rank,
                    player.getName(),
                    run.getDurationSeconds(),
                    run.getFinalHp(),
                    bossFight.getTotalTurns(),
                    deck.getSize()
            ));
        }

        return new RankingResponse(
                seasonId,
                totalRecords,
                excludedCount,
                entries
        );
    }

    private static final Comparator<RankedRecord> RANKING_ORDER
            = Comparator.comparingInt((RankedRecord r) -> r.getRun().getDurationSeconds()) // 클리어 시간 오름차순
                        .thenComparing(Comparator.comparingInt(
                                (RankedRecord r) -> r.getRun().getFinalHp()).reversed()) // 남은 체력 내림차순
                        .thenComparingLong(RankedRecord::getId); // 레코드 ID 오름차순
}
