package com.gamebasic.ranking.service;

import com.gamebasic.ranking.dto.source.RankedBossFight;
import com.gamebasic.ranking.dto.source.RankedBossPhase;
import com.gamebasic.ranking.dto.source.RankedCard;
import com.gamebasic.ranking.dto.source.RankedDeck;
import com.gamebasic.ranking.dto.source.RankedRecord;
import com.gamebasic.ranking.dto.source.RankedRun;
import com.gamebasic.runcard.entity.CardType;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class RankingRecordValidator {

    // 인스턴스화 차단
    private RankingRecordValidator() {
        throw new AssertionError();
    }

    private static final List<String> BOSS_PHASE_ORDER = List.of("THRONE", "UNBOUND", "ECLIPSE");

    public static boolean checkRecordValidity(RankedRecord record) {
        if (record == null)
            return false;

        // 보스 마무리 카드 검증을 위한 카드 집합
        Set<String> deckCardTypes = record.getDeck().getCards().stream()
                .map(RankedCard::getCardType)
                .collect(Collectors.toSet());

        // 모든 검증 항목 통과 시 true 반환
        return checkRunValidity(record.getRun())
                && checkDeckValidity(record.getDeck())
                && checkBossFightValidity(record.getBossFight(), deckCardTypes);
    }

    private static boolean checkRunValidity(RankedRun run) {
        if (run == null)
            return false;

        // 클리어 시간
        if (run.getDurationSeconds() < run.getClearedFloor() * 30)
            return false;

        // 남은 HP
        int finalHp = run.getFinalHp();
        return finalHp >= 1 && finalHp <= 99;
    }

    private static boolean checkDeckValidity(RankedDeck deck) {
        if (deck == null)
            return false;

        // 카드 타입 및 획득 층
        List<RankedCard> cards = deck.getCards();
        for (RankedCard card : cards) {
            if (!checkCardValidity(card))
                return false;
        }

        // 덱 크기
        int deckSize = deck.getSize();
        return deckSize >= 9 && deckSize <= 20 && deckSize == cards.size();
    }

    private static boolean checkBossFightValidity(RankedBossFight bossFight, Set<String> cardTypes) {
        if (bossFight == null)
            return false;

        List<RankedBossPhase> phases = bossFight.getPhases();
        String finishingCard = bossFight.getFinishingCard();
        int totalTurns = bossFight.getTotalTurns();

        // 보스 페이즈: 개수
        if (phases.size() != BOSS_PHASE_ORDER.size())
            return false;

        for (int i = 0; i < phases.size(); i++) {
            RankedBossPhase phase = phases.get(i);

            // 보스 페이즈: 이름
            if (!Objects.equals(phase.getPhase(), BOSS_PHASE_ORDER.get(i)))
                return false;

            // 보스 페이즈: 유효 턴 수
            int turns = phase.getTurns();
            if (turns < 1)
                return false;

            totalTurns -= turns;
        }

        // 보스 페이즈: 턴 수 일관성
        if (totalTurns != 0)
            return false;

        // 마무리 카드
        return cardTypes.contains(finishingCard);
    }

    private static boolean checkCardValidity(RankedCard card) {
        if (card == null)
            return false;

        // 카드 타입
        String cardType = card.getCardType();
        if (cardType == null)
            return false;

        try {
            CardType.valueOf(cardType);
        } catch (IllegalArgumentException e) {
            return false;
        }

        // 획득 층
        int acquiredFloor = card.getAcquiredFloor();
        return acquiredFloor >= 0 && acquiredFloor <= 9;
    }
}
