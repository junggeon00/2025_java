package cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CardCase {
	 private List<Card> cards;

	    // 카드 총 장수 (4문양 * 13 숫자)
	    private static final int NUMOFCARDS = Card.DECK.length * Card.STECK.length;

	    public CardCase() {
	        cards = new ArrayList<>();
	        initDeck();
	        Collections.shuffle(cards);  // 셔플!
	    }

	    // 52장 카드 덱 생성 (중복 없이)
	    private void initDeck() {
	        for (String suit : Card.DECK) {
	            for (String rank : Card.STECK) {
	                cards.add(new Card(suit, rank));
	            }
	        }
	    }

	    // 카드 뽑기 (덱에서 한 장 꺼내기)
	    public Card drawCard() {
	        if (cards.isEmpty()) {
	            return null; // 카드 없으면 null 반환
	        }
	        return cards.remove(0);
	    }

	    // 전체 카드 리스트 반환 (필요시)
	    public List<Card> getCards() {
	        return cards;
	    }
	    
	    //남은 카드 수 확인
	    public int remainingCards() {
	        return cards.size();
	    }
	    
	    //덱 초기화 및 다시 섞기
	    public void reset() {
	        cards.clear();
	        initDeck();
	        Collections.shuffle(cards);
	    }
	    
	    //카드 여러 장 한 번에 뽑기
	    public List<Card> drawCards(int n) {
	        List<Card> drawn = new ArrayList<>();
	        for (int i = 0; i < n; i++) {
	            Card c = drawCard();
	            if (c != null) {
	                drawn.add(c);
	            } else {
	                break;
	            }
	        }
	        return drawn;
	    }



}
