import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main { //cas -525: -25: 25: 75

    //As of the email, I understand that the players' legal actions won't be calculated into the casino balance, even if they made legal actions BEFORE making an illegal operation.

    public static void main(String[] args) {
        HashMap<String, Match> matchResults = readMatchesData("match_data.txt");
        ReturnPlayers values = readPlayersData("player_data.txt", matchResults);
        writeOutput(values.getLegal(), values.getIllegal());

    }

    private static void writeOutput(HashMap<String,Player> legalPlayers, HashMap<String,String> illegalPlayers1){
        try {
            PrintWriter output = new PrintWriter("result.txt");
            int casinoBalance = 0;

            for (Player player : legalPlayers.values()) {
                output.println(player.getId() +" "+ player.getBalance() + " " + player.getRatio());
                casinoBalance -= player.getBetsBalance();
            }
            output.println();

            for (String string : illegalPlayers1.values()) {
                output.println(string);
            }
            output.println();
            output.println(casinoBalance);
            output.close();
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, Match> readMatchesData(String matchDataFile) {
        HashMap<String, Match> matchResults = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File(matchDataFile));
            while (scanner.hasNextLine()) {
                /*
                1 - matchID
                2 - return odds A
                3 - return odds B
                4 - match result
                 */
                String[] matchInfo = scanner.nextLine().split(",");
                matchResults.put(
                        matchInfo[0],
                        new Match(matchInfo[0],
                                Double.parseDouble(matchInfo[1]),
                                Double.parseDouble(matchInfo[2]),
                                matchInfo[3]));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return matchResults;
    }

    public static ReturnPlayers readPlayersData(String playerDataFile, HashMap<String, Match> matchResults) {
        //TODO Current problem: Illegal player actions are handled, if they were made before the illegal actions
        //SOlUTION: handle casino balance after all the bets, add winnings/losses bal to player?
        HashMap<String, Player> players = new HashMap<>();
        HashMap<String, String> illegalPlayers = new HashMap<>();

        try {
            Scanner scanner = new Scanner(new File(playerDataFile));
            while (scanner.hasNextLine()) {
                /* Player data input format
                 0 - player ID
                 1 - type(BET/WITHDRAW/DEPOSIT)
                 2 - match id(only if bet)
                 3 - amount
                 4-  bet side(only if bet)
                 */
                String[] split = scanner.nextLine().split(","); //Parse the player data line
                String playerID = split[0];
                String matchID = split[2];
                int amount = Integer.parseInt(split[3]);


                //Create new player if not yet existing
                players.putIfAbsent(playerID, new Player(playerID));

                Player currentPlayer = players.get(playerID);

                //Only process the action if player hasn't made any illegal actions
                if (!illegalPlayers.containsKey(playerID)) {
                    switch (split[1]) {
                        case "DEPOSIT":
                            currentPlayer.deposit(amount);
                            break;
                        case "WITHDRAW":
                            if (currentPlayer.validateBalance(amount)) {
                                currentPlayer.withdraw(amount);
                            } else {
                                players.remove(playerID);
                                illegalPlayers.putIfAbsent(playerID, (playerID + " WITHDRAW null " + amount + " null"));
                            }
                            break;
                        case "BET":
                            String betSide = split[4];
                            if (currentPlayer.validateBalance(amount)) {
                                Match currentMatch = matchResults.get(matchID);
                                currentPlayer.incrementTotalBets();

                                if (currentMatch.getResult().equals("DRAW")){ //In the case of a draw, neither the casino's or player's balance changes
                                    break;
                                }else if(currentMatch.getResult().equals(betSide)){
                                    int winnings = (int) (amount*currentMatch.getReturnOdds(betSide));
                                    currentPlayer.betWin(winnings);
                                }else{
                                    currentPlayer.betLoss(amount);
                                }
                            } else {
                                players.remove(playerID);
                                illegalPlayers.putIfAbsent(playerID, (playerID + " BET " + split[2] + " " + amount + " " + split[4]));
                            }
                            break;
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new ReturnPlayers(players, illegalPlayers);

    }

}