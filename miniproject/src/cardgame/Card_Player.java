package cardgame;

import java.util.ArrayList;
import java.util.List;

public class Card_Player {

    private String name;
    private List<Card> hand = new ArrayList<>();

    //추가된 필드
    private boolean folded = false;      // 다이 여부
    private Card openCard;               // 공개된 카드
    private Card discardedCard;          // 버린 카드
    private int score = -1;              // 족보 점수 저장 (선택)
    
    private BettingSystem bettingSystem;

    public Card_Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.bettingSystem = new BettingSystem(1000000);
    }

    //카드 받기 (최대 7장까지)
    public void receiveCard(Card card) {
        if (hand.size() < 7) {
            hand.add(card);
        }
    }

    //현재 카드 목록 반환
    public List<Card> getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    //카드 목록 보기 좋게 출력
    public void showHand() {
        System.out.print(name + "의 카드: ");
        for (Card card : hand) {
            System.out.print(card + " ");
        }
        System.out.println();
    }

    //카드 버리기
    public void discardCard(Card card) {
        if (hand.contains(card)) {
            hand.remove(card);
            this.discardedCard = card;
        }
    }

    //오픈 카드 설정
    public void setOpenCard(Card card) {
        if (hand.contains(card)) {
            this.openCard = card;
        }
    }

    //오픈 카드 조회
    public Card getOpenCard() {
        return openCard;
    }

    //다이 선언
    public void fold() {
        this.folded = true;
    }

    //다이 여부 확인
    public boolean hasFolded() {
        return folded;
    }

    //족보 점수 저장
    public void setScore(int score) {
        this.score = score;
    }

    //족보 점수 조회
    public int getScore() {
        return score;
    }

    //버린 카드 조회 (디버깅용 등)
    public Card getDiscardedCard() {
        return discardedCard;
    }
    
    

    
    public BettingSystem getBettingSystem() {
        return bettingSystem;
    }

}
