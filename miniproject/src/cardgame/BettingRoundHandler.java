package cardgame;

import java.util.*;

public class BettingRoundHandler {

    public List<Card_Player> bettingRound(Scanner sc, List<Card_Player> players, int cardCount) {
        int currentMaxBet = 0;
        Card_Player raiser = null;

        ListIterator<Card_Player> iter = players.listIterator();

        while (iter.hasNext()) {
            Card_Player p = iter.next();

            CardPrinter.clearScreen();
            System.out.printf("%s의 현재 카드 (%d장):\n", p.getName(), cardCount);
            CardPrinter.printCards(p, p);
            System.out.println("현재 잔액: " + p.getBettingSystem().getBalance() + "원");

            // 첫 베팅자
            if (raiser == null) {
                System.out.println("베팅 옵션: 1) 쿼터  2) 하프  3) 올인  4) 다이");
                System.out.print("선택 (1~4): ");
                int choice = Integer.parseInt(sc.nextLine());

                int balance = p.getBettingSystem().getBalance();
                int betAmount = 0;

                switch (choice) {
                    case 1 -> betAmount = balance / 4;
                    case 2 -> betAmount = balance / 2;
                    case 3 -> betAmount = balance;
                    case 4 -> {
                        System.out.println("다이 선택. 탈락합니다.");
                        iter.remove();
                        continue;
                    }
                    default -> {
                        System.out.println("잘못된 선택. 탈락합니다.");
                        iter.remove();
                        continue;
                    }
                }

                if (p.getBettingSystem().placeBet(betAmount)) {
                    currentMaxBet = betAmount;
                    raiser = p;
                } else {
                    System.out.println("베팅 실패. 탈락합니다.");
                    iter.remove();
                }
            } else {
                // 이후 플레이어: 콜 / 다이 / 레이즈
                int toCall = currentMaxBet - p.getBettingSystem().getCurrentBet();
                System.out.printf("현재 베팅 금액: %d원, 내 잔액: %d원\n", currentMaxBet, p.getBettingSystem().getBalance());
                System.out.println("선택: 1) 콜  2) 다이  3) 레이즈(하프)");

                System.out.print("선택 (1~3): ");
                int choice = Integer.parseInt(sc.nextLine());

                int balance = p.getBettingSystem().getBalance();

                switch (choice) {
                    case 1 -> { // 콜
                        if (!p.getBettingSystem().placeBet(toCall)) {
                            System.out.println("콜 실패. 탈락합니다.");
                            iter.remove();
                        }
                    }
                    case 2 -> { // 다이
                        System.out.println("다이 선택. 탈락합니다.");
                        iter.remove();
                    }
                    case 3 -> { // 레이즈
                        int raiseAmount = balance / 2;
                        if (raiseAmount <= currentMaxBet) {
                            System.out.println("⚠ 현재 베팅보다 높아야 합니다. 레이즈 무효 → 다이 처리.");
                            iter.remove();
                            continue;
                        }

                        if (p.getBettingSystem().placeBet(raiseAmount)) {
                            currentMaxBet = raiseAmount;
                            raiser = p;
                            // ✅ 새로운 레이즈 → 다시 처음부터 순회
                            iter = players.listIterator();
                        } else {
                            System.out.println("레이즈 실패. 탈락합니다.");
                            iter.remove();
                        }
                    }
                    default -> {
                        System.out.println("잘못된 선택. 탈락합니다.");
                        iter.remove();
                    }
                }
            }
        }

        return players;
    }
}
