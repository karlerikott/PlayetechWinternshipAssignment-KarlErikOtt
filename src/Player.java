public class Player {
    private String id;

    private int balance;
    private int totalBets;
    private int winningBets;

    private int betsBalance;  //We use this to calculate casino balance afterwards


    public Player(String id) {
        this.id = id;
        this.balance = 0;
        this.totalBets = 0;
        this.winningBets = 0;
        this.betsBalance = 0;
    }

    public int getBetsBalance() {
        return betsBalance;
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void deposit(int amount) {
        this.balance += amount;
    }

    public boolean validateBalance(int sum) {
        return this.balance > sum;
    }



    public void incrementTotalBets() {
        this.totalBets++;
    }

    public void withdraw(int amount) {
        this.balance -= amount;
    }

    public void betWin(int amount) {
        this.betsBalance += amount;
        this.balance += amount;
        this.winningBets++;
    }

    public void betLoss(int amount) {
        this.betsBalance -= amount;
        this.balance -= amount;
    }

    public String getRatio() {
        return Double.toString(Math.round((double) this.winningBets /this.totalBets * 100.0)/100.0);
    }
}
