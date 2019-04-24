package Messages;

public class LoginInfo {

    private String username;
    private String password;
    private String screenName;


    public LoginInfo(String username, String password, String screenName) {

        this.username = username;
        this.password = password;
        this.screenName = screenName;

    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getScreenName()
    {
        return screenName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }




}
