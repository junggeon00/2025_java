package cardgame;

import java.util.*;

public class PokerHandEvaluator {
    public enum HandRank {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT,
        FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH
    }

    // 족보 평가 결과 클래스
    public static class HandResult implements Comparable<HandResult> {
        private HandRank rank;
        private List<Integer> rankValues; // tie breaker용 숫자들 (내림차순)

        public HandResult(HandRank rank, List<Integer> rankValues) {
            this.rank = rank;
            this.rankValues = rankValues;
        }

        public HandRank getRank() {
            return rank;
        }

        // 족보 비교
        @Override
        public int compareTo(HandResult o) {
            if (this.rank.ordinal() != o.rank.ordinal()) {
                return this.rank.ordinal() - o.rank.ordinal();
            }
            for (int i = 0; i < Math.min(this.rankValues.size(), o.rankValues.size()); i++) {
                int cmp = this.rankValues.get(i) - o.rankValues.get(i);
                if (cmp != 0) return cmp;
            }
            return 0;
        }

        @Override
        public String toString() {
            return rank + " " + rankValues;
        }
    }

    // 메인 평가 함수: 7장 카드 리스트 중 최선 5장 족보 리턴
    public static HandResult evaluate(List<Card> cards) {
        if (cards.size() < 5) {
            throw new IllegalArgumentException("카드가 5장 이상이어야 합니다.");
        }

        List<List<Card>> combos = combinations(cards, 5);
        HandResult best = null;

        for (List<Card> combo : combos) {
            HandResult hr = evaluateFiveCards(combo);
            if (best == null || hr.compareTo(best) > 0) {
                best = hr;
            }
        }

        return best;
    }

    // 5장 카드 족보 평가
    private static HandResult evaluateFiveCards(List<Card> hand) {
        List<Integer> ranks = new ArrayList<>();
        Map<Integer, Integer> rankCount = new HashMap<>();
        Map<String, Integer> suitCount = new HashMap<>();

        for (Card c : hand) {
            int r = rankToInt(c.getRank());
            ranks.add(r);
            rankCount.put(r, rankCount.getOrDefault(r, 0) + 1);
            suitCount.put(c.getSuit(), suitCount.getOrDefault(c.getSuit(), 0) + 1);
        }
        Collections.sort(ranks, Collections.reverseOrder());

        boolean flush = suitCount.values().stream().anyMatch(count -> count >= 5);
        boolean straight = isStraight(ranks);

        // ✅ 스트레이트 플러시 / 로열 플러시
        if (flush && straight) {
            String flushSuit = getFlushSuit(hand);
            List<Integer> flushRanks = new ArrayList<>();
            for (Card c : hand) {
                if (c.getSuit().equals(flushSuit)) {
                    flushRanks.add(rankToInt(c.getRank()));
                }
            }
            Collections.sort(flushRanks, Collections.reverseOrder());

            if (flushRanks.containsAll(Arrays.asList(14, 13, 12, 11, 10))) {
                return new HandResult(HandRank.ROYAL_FLUSH, flushRanks);
            }
            if (isStraight(flushRanks)) {
                return new HandResult(HandRank.STRAIGHT_FLUSH, List.of(flushRanks.get(0)));
            }
        }

        if (rankCount.containsValue(4)) {
            int fourRank = getKeyByValue(rankCount, 4);
            List<Integer> kickers = getKickers(rankCount, List.of(fourRank));
            List<Integer> rankVals = new ArrayList<>();
            rankVals.add(fourRank);
            rankVals.addAll(kickers);
            return new HandResult(HandRank.FOUR_OF_A_KIND, rankVals);
        }

        if (rankCount.containsValue(3) && rankCount.containsValue(2)) {
            int threeRank = getKeyByValue(rankCount, 3);
            int pairRank = getKeyByValue(rankCount, 2);
            return new HandResult(HandRank.FULL_HOUSE, List.of(threeRank, pairRank));
        }

        if (flush) {
            return new HandResult(HandRank.FLUSH, ranks);
        }

        if (straight) {
            return new HandResult(HandRank.STRAIGHT, List.of(Collections.max(ranks)));
        }

        if (rankCount.containsValue(3)) {
            int threeRank = getKeyByValue(rankCount, 3);
            List<Integer> kickers = getKickers(rankCount, List.of(threeRank));
            List<Integer> rankVals = new ArrayList<>();
            rankVals.add(threeRank);
            rankVals.addAll(kickers);
            return new HandResult(HandRank.THREE_OF_A_KIND, rankVals);
        }

        List<Integer> pairs = getKeysByValue(rankCount, 2);
        if (pairs.size() >= 2) {
            Collections.sort(pairs, Collections.reverseOrder());
            List<Integer> kickers = getKickers(rankCount, pairs);
            List<Integer> rankVals = new ArrayList<>();
            rankVals.addAll(pairs);
            rankVals.addAll(kickers);
            return new HandResult(HandRank.TWO_PAIR, rankVals);
        }

        if (pairs.size() == 1) {
            List<Integer> kickers = getKickers(rankCount, pairs);
            List<Integer> rankVals = new ArrayList<>();
            rankVals.addAll(pairs);
            rankVals.addAll(kickers);
            return new HandResult(HandRank.ONE_PAIR, rankVals);
        }

        return new HandResult(HandRank.HIGH_CARD, ranks);
    }

    public static int rankToInt(String rank) {
        return switch (rank) {
            case "A" -> 14;
            case "K" -> 13;
            case "Q" -> 12;
            case "J" -> 11;
            default -> Integer.parseInt(rank);
        };
    }

    private static List<Integer> getKickers(Map<Integer, Integer> rankCount, List<Integer> excludeRanks) {
        List<Integer> kickers = new ArrayList<>();
        for (int r = 14; r >= 2; r--) {
            if (!excludeRanks.contains(r) && rankCount.containsKey(r)) {
                kickers.add(r);
            }
        }
        return kickers;
    }

    private static int getKeyByValue(Map<Integer, Integer> map, int value) {
        return map.entrySet()
                .stream()
                .filter(e -> e.getValue() == value)
                .map(Map.Entry::getKey)
                .max(Integer::compare)
                .orElse(0);
    }

    private static List<Integer> getKeysByValue(Map<Integer, Integer> map, int value) {
        List<Integer> keys = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : map.entrySet()) {
            if (e.getValue() == value) {
                keys.add(e.getKey());
            }
        }
        return keys;
    }

    private static boolean isStraight(List<Integer> ranks) {
        Set<Integer> set = new HashSet<>(ranks);
        if (set.containsAll(List.of(14, 2, 3, 4, 5))) return true;

        int consecutive = 1;
        int last = -1;
        for (int r : ranks) {
            if (last == -1 || last - 1 == r) {
                consecutive++;
            } else if (last != r) {
                consecutive = 1;
            }
            last = r;
            if (consecutive >= 5) return true;
        }
        return false;
    }

    private static List<List<Card>> combinations(List<Card> arr, int n) {
        List<List<Card>> result = new ArrayList<>();
        combine(arr, n, 0, new ArrayList<>(), result);
        return result;
    }

    private static void combine(List<Card> arr, int n, int start, List<Card> temp, List<List<Card>> result) {
        if (temp.size() == n) {
            result.add(new ArrayList<>(temp));
            return;
        }
        for (int i = start; i < arr.size(); i++) {
            temp.add(arr.get(i));
            combine(arr, n, i + 1, temp, result);
            temp.remove(temp.size() - 1);
        }
    }

    // ✅ 보조: 플러시 슈트 찾기
    private static String getFlushSuit(List<Card> hand) {
        Map<String, Integer> count = new HashMap<>();
        for (Card c : hand) {
            count.put(c.getSuit(), count.getOrDefault(c.getSuit(), 0) + 1);
        }
        for (String suit : count.keySet()) {
            if (count.get(suit) >= 5) return suit;
        }
        return null;
    }

    // ✅ 족보 한글 이름 반환
    public static String getHandRankName(HandRank rank) {
        return switch (rank) {
            case ROYAL_FLUSH -> "로열 플러시";
            case STRAIGHT_FLUSH -> "스트레이트 플러시";
            case FOUR_OF_A_KIND -> "포카드";
            case FULL_HOUSE -> "풀하우스";
            case FLUSH -> "플러시";
            case STRAIGHT -> "스트레이트";
            case THREE_OF_A_KIND -> "트리플";
            case TWO_PAIR -> "투페어";
            case ONE_PAIR -> "원페어";
            default -> "하이카드";
        };
    }
}
