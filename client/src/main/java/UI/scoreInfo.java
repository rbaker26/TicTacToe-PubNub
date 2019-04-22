package UI;

public class scoreInfo {

    private String opponent;
    private String status;

    public scoreInfo(String opponent, String status) {

        super();

        this.opponent = opponent;
        this.status = status;

    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
