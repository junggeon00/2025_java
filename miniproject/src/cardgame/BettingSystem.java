package cardgame;

public class BettingSystem {
    private int balance;
    private int currentBet;

    public BettingSystem(int initialBalance) {
        this.balance = initialBalance;
        this.currentBet = 0;
    }

    // 잔액 반환
    public int getBalance() {
        return balance;
    }

    // 현재 베팅 금액 반환
    public int getCurrentBet() {
        return currentBet;
    }

    // 베팅 수행
    public boolean placeBet(int amount) {
        if (amount <= balance) {
            balance -= amount;
            currentBet += amount;
            return true;
        }
        return false;
    }

    // 승리 시 상금 획득
    public void winPot(int pot) {
        balance += pot;
        currentBet = 0;
    }

    // 패배 시 베팅 금액 초기화
    public void loseBet() {
        currentBet = 0;
    }
}
