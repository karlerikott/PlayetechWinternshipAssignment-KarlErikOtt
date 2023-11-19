import java.util.HashMap;

public class ReturnPlayers {
     HashMap<String, Player> legal;
     HashMap<String, String> illegal;

    public ReturnPlayers(HashMap<String, Player> legal, HashMap<String, String> illegal) {
        this.legal = legal;
        this.illegal = illegal;
    }

    public HashMap<String, Player> getLegal() {
        return legal;
    }

    public HashMap<String, String> getIllegal() {
        return illegal;
    }
}
