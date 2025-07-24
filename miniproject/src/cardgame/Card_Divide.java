package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Card_Divide {
    
    public static final String[] DECK = {"◆", "♥", "♣", "♠"};
    public static final String[] STECK = {
        "A", "2", "3", "4", "5", "6", "7",
        "8", "9", "10", "J", "Q", "K"
    };

    //카드 덱 생성
    public static List<String> makeDeck() {
        List<String> deck = new ArrayList<>();
        for (String shape : DECK) {
            for (String number : STECK) {
                deck.add(shape + number);
            }
        }
        return deck;
    }

    //실제 Card 객체로 덱 생성 및 분배
    public static Map<String, List<Card>> divide(int playerCount, int cardsPerPlayer) {
        List<Card> deck = new ArrayList<>();
        for (String suit : DECK) {
            for (String rank : STECK) {
                deck.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(deck);

        Map<String, List<Card>> playerMap = new HashMap<>();
        for (int i = 1; i <= playerCount; i++) {
            List<Card> hand = new ArrayList<>();
            for (int j = 0; j < cardsPerPlayer; j++) {
                if (!deck.isEmpty()) {
                    hand.add(deck.remove(0));
                }
            }
            playerMap.put("Player " + i, hand);
        }

        return playerMap;
    }
}
