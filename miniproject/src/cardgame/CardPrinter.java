package cardgame;

import java.util.*;

public class CardPrinter {

	public static void printCards(Card_Player viewer, Card_Player owner) {
        List<Card> cards = owner.getHand();
        for (int i = 0; i < cards.size(); i++) {
            boolean isHidden = viewer != owner && (
                (cards.size() == 5 || cards.size() == 6) && (i == 0 || i == 1)
                || (cards.size() == 7) && (i == 0 || i == 1 || i == 6)
            );
            String out = isHidden ? "[??]" : cards.get(i).toString();
            System.out.printf("%-8s", out);
        }
        System.out.println();
    }

    public static void clearScreen() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    public void printAllPlayerCards(List<Card_Player> players, Scanner sc) {
        for (Card_Player viewer : players) {
            clearScreen();
            System.out.println("[" + viewer.getName() + " 시점]");
            for (Card_Player target : players) {
                System.out.print(target.getName() + ": ");
                printCards(viewer, target);
            }
            System.out.print("Enter로 계속...");
            sc.nextLine();
        }
    }
}
