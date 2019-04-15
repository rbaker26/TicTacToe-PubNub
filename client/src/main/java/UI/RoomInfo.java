package UI;

public class RoomInfo {

    private String id; //5 digit ID number
    private String lobbyStatus; //open or closed
    private String player1; //name
    private String player2; //name
    private String player1Token; //X or O
    private String player2Token; //X or O

    public RoomInfo(String id, String lobbyStatus, String player1, String player2,
                    String player1Token, String player2Token) {

        super();

        this.id = id;
        this.lobbyStatus = lobbyStatus;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Token = player1Token;
        this.player2Token = player2Token;

    }

    public String getID() {
        return id;
    }

    public String getLobbyStatus() {
        return lobbyStatus;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public String getPlayer1Token() { return player1Token; }

    public String getPlayer2Token() { return player2Token; }

    public void setID(String id) {
        this.id = id;
    }

    public void setLobbyStatus(String lobbyStatus) {
        this.lobbyStatus = lobbyStatus;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public void setPlayer1Token(String player1Token) { this.player1Token = player1Token; }

    public void setPlayer2Token(String player2Token) { this.player2Token = player2Token; }


}

