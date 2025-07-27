package cardgame;
import java.util.*;

public class CardMain {
    private static final int PLAYER_COUNT = 3;

    private static void printCards(Card_Player viewer, Card_Player owner) {
        List<Card> cards = owner.getHand();
        int cardCount = cards.size();

        for (int i = 0; i < cardCount; i++) {
            String out;
            if (viewer == owner) {
                out = cards.get(i).toString();
            } else {
                boolean isHidden = false;
                if (cardCount == 5 || cardCount == 6) {
                    if (i == 0 || i == 1) isHidden = true;
                } else if (cardCount == 7) {
                    if (i == 0 || i == 1 || i == 6) isHidden = true;
                }
                out = isHidden ? "[??]" : cards.get(i).toString();
            }

            // ✅ 출력 정렬: 카드 하나당 8자리 확보
            System.out.printf("%-8s", out);
        }
        System.out.println();
    }



    private static void printBlankLines(int n) {
        for (int i = 0; i < n; i++) System.out.println();
    }

    public static void clearScreen() {
        for (int i = 0; i < 50; i++) System.out.println();
        
    }
    
 // 모든 플레이어의 카드를 각자 시점에서 출력 (자기자신이 맨 위)
    private static void printAllPlayerCards(List<Card_Player> players, Scanner sc) {
        for (Card_Player viewer : players) {
            clearScreen();
            System.out.println("[" + viewer.getName() + " 시점]");

            List<Card_Player> ordered = new ArrayList<>();
            ordered.add(viewer);
            for (Card_Player p : players) {
                if (p != viewer) ordered.add(p);
            }

            for (Card_Player target : ordered) {
                System.out.print(target.getName() + ": ");
                printCards(viewer, target);
            }

            System.out.print("Enter로 계속...");
            sc.nextLine();
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CardCase deck = new CardCase();

        List<Card_Player> players = new ArrayList<>();
        for (int i = 1; i <= PLAYER_COUNT; i++) {
            players.add(new Card_Player("Player" + i));
        }

        // 초기 4장 배분
        for (Card_Player p : players) {
            for (int i = 0; i < 4; i++) {
                p.receiveCard(deck.drawCard());
            }
        }

        // 카드 1장 버리기
        for (Card_Player p : players) {
            clearScreen();
            System.out.println(p.getName() + "의 초기 카드:");
            printCards(p, p);
            System.out.print("버릴 카드 인덱스 (1~4): ");
            int index = Integer.parseInt(sc.nextLine()) - 1;
            p.getHand().remove(index);
        }

        // 공개 카드 선택
        Map<Card_Player, Card> openCards = new HashMap<>();
        for (Card_Player p : players) {
            clearScreen();
            System.out.println(p.getName() + "의 현재 카드:");
            printCards(p, p);
            System.out.print("공개할 카드 인덱스 선택 (1~3): ");
            int openIndex = Integer.parseInt(sc.nextLine()) - 1;
            Card open = p.getHand().get(openIndex);
            openCards.put(p, open);
        }

        // 공개 카드 높은 순 정렬
        players.sort((a, b) -> {
            Card ca = openCards.get(a);
            Card cb = openCards.get(b);
            return Integer.compare(
                PokerHandEvaluator.rankToInt(cb.getRank()),
                PokerHandEvaluator.rankToInt(ca.getRank())
            );
        });

        List<Card_Player> bettingPlayers = new ArrayList<>(players);
        Map<Card_Player, PokerHandEvaluator.HandResult> handResults = new HashMap<>();

        // 4~5번째 카드 지급
        for (Card_Player p : bettingPlayers) {
            clearScreen();
            p.receiveCard(deck.drawCard());
            p.receiveCard(deck.drawCard());
        }

        printAllPlayerCards(players, sc);



        // 5장 받은 후 베팅
        Iterator<Card_Player> iter = bettingPlayers.iterator();
        while (iter.hasNext()) {
            Card_Player p = iter.next();
            clearScreen();
            System.out.println(p.getName() + "의 현재 카드 (5장):");
            printCards(p, p);
            System.out.print("계속 베팅하시겠습니까? (y/n): ");
            if (!sc.nextLine().equalsIgnoreCase("y")) {
                iter.remove();
            }
        }

        if (bettingPlayers.isEmpty()) {
            clearScreen();
            System.out.println("⚠ 아무도 베팅하지 않아 게임 종료");
            return;
        }

        // 6번째 카드 지급
        for (Card_Player p : bettingPlayers) {
            p.receiveCard(deck.drawCard());
        }

        printAllPlayerCards(players, sc);


        // 6장 받은 후 베팅
        iter = bettingPlayers.iterator();
        while (iter.hasNext()) {
            Card_Player p = iter.next();
            clearScreen();
            System.out.println(p.getName() + "의 현재 카드 (6장):");
            printCards(p, p);
            System.out.print("계속 베팅하시겠습니까? (y/n): ");
            if (!sc.nextLine().equalsIgnoreCase("y")) {
                iter.remove();
            }
        }

        if (bettingPlayers.isEmpty()) {
            clearScreen();
            System.out.println("⚠ 아무도 베팅하지 않아 게임 종료");
            return;
        }

        // 7번째 카드 지급
        for (Card_Player p : bettingPlayers) {
            p.receiveCard(deck.drawCard());
        }

        printAllPlayerCards(players, sc);


        // 7장 받은 후 최종 베팅
        iter = bettingPlayers.iterator();
        while (iter.hasNext()) {
            Card_Player p = iter.next();
            clearScreen();
            System.out.println(p.getName() + "의 최종 카드 (7장):");
            printCards(p, p);
            System.out.print("최종 베팅하시겠습니까? (y/n): ");
            if (!sc.nextLine().equalsIgnoreCase("y")) {
                iter.remove();
            }
        }

        if (bettingPlayers.isEmpty()) {
            clearScreen();
            System.out.println("⚠ 아무도 베팅하지 않아 게임 종료");
            return;
        }

        // 족보 평가
        clearScreen();
        Card_Player winner = null;
        PokerHandEvaluator.HandResult best = null;

        for (Card_Player p : bettingPlayers) {
            System.out.println(p.getName() + "의 전체 카드:");
            printCards(p, p);
            PokerHandEvaluator.HandResult result = PokerHandEvaluator.evaluate(p.getHand());
            handResults.put(p, result);
            System.out.println("족보: " + result.getRank());
            System.out.println("----------------------------------");

            if (best == null || result.compareTo(best) > 0) {
                best = result;
                winner = p;
            }
        }

        // 결과 발표
        if (winner != null) {
            System.out.printf("\n🎉 승자: %s (%s)\n", winner.getName(), best.getRank());
        } else {
            System.out.println("⚠ 베팅한 플레이어가 없어 승자 없음");
        }

        System.out.println("\n게임 종료");
        sc.close();
    }
}
