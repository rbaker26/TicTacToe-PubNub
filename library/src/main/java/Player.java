public class Player {

    public String name = "";
    public int playerID = 0;

    public Player(String name, int playerID) {
        this.name = name;
        this.playerID = playerID;
    }

    @Override
    public String toString() {
        return "Name:\t" + this.name + "\tplayerID:\t" + String.valueOf(this.playerID);
    }
}
