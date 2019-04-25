package UI;

public class scoreInfo {

    private String username;
    private Integer gamesWon;
    private Integer gamesPlayed;

    public scoreInfo(String username, Integer gamesWon, Integer gamesPlayed) {

        super();

        this.username = username;
        this.gamesWon = gamesWon;
        this.gamesPlayed = gamesPlayed;

    }

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public Integer getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(Integer gamesWon) {
        this.gamesWon = gamesWon;
    }

    public Integer getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(Integer gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

}
