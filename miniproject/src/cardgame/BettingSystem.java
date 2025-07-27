package cardgame;

public class BettingSystem {
	private int balance;    // 현재 플레이어가 가진 돈
    private int currentBet; // 이번 게임에서 베팅한 금액

    public BettingSystem(int initialBalance) {
        this.balance = initialBalance;
        this.currentBet = 0;
    }
//
    // 베팅 시도
    public boolean placeBet(int amount) {
        if (amount > balance || amount <= 0) {
            System.out.println("베팅 실패: 잔액 부족 또는 잘못된 금액.");
            return false;
        }
        currentBet += amount;
        balance -= amount;
        System.out.println("베팅 성공: " + amount + "원 (잔액: " + balance + "원)");
        return true;
    }

    // 이겼을 때 상금 지급 (판돈 받음)
    public void winPot(int pot) {
        balance += pot;
        currentBet = 0;
        System.out.println("축하합니다!");
    }

    // 졌을 때 베팅금 초기화
    public void loseBet() {
        System.out.println("베팅 금액 " + currentBet + "원 손실.");
        currentBet = 0;
    }

    public int getBalance() {
        return balance;
    }

    public int getCurrentBet() {
        return currentBet;
    }
}
