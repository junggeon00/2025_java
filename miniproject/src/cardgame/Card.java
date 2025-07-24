package cardgame;

import java.util.Objects;

public class Card implements Comparable<Card> {

    public static final String[] DECK = {"◆", "♥", "♣", "♠"};
    public static final String[] STECK = {
        "A", "2", "3", "4", "5", "6", "7",
        "8", "9", "10", "J", "Q", "K"
    };

    private String suit;  // 문양
    private String rank;  // 숫자 or 문자

    // 무작위 카드 생성
    public Card() {
        int a = (int)(Math.random() * DECK.length);
        int b = (int)(Math.random() * STECK.length);
        this.suit = DECK[a];
        this.rank = STECK[b];
    }

    // 명시적 카드 생성
    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    /**
     * rank를 정수값으로 변환 (A=14, K=13, ... 2=2)
     */
    public int getRankValue() {
        return switch (rank) {
            case "A" -> 14;
            case "K" -> 13;
            case "Q" -> 12;
            case "J" -> 11;
            default -> Integer.parseInt(rank);
        };
    }

    /**
     * 문양이 같은지 비교
     */
    public boolean isSameSuit(Card other) {
        return this.suit.equals(other.suit);
    }

    /**
     * 카드 크기 비교 (rank 기준)
     */
    @Override
    public int compareTo(Card other) {
        return Integer.compare(this.getRankValue(), other.getRankValue());
    }

    /**
     * 카드 출력: [♠ K]
     */
    @Override
    public String toString() {
        String value = rank.length() == 1 ? " " + rank : rank;
        return "[" + suit + value + "]";
    }

    /**
     * 동일한 문양 + 숫자일 경우 같은 카드로 간주
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Card)) return false;
        Card other = (Card) obj;
        return this.suit.equals(other.suit) && this.rank.equals(other.rank);
    }

    @Override
    public int hashCode() {
        return Objects.hash(suit, rank);
    }
}
