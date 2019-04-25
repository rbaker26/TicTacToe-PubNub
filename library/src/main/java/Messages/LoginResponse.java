package Messages;

public class LoginResponse {
    LoginInfo loginRequest;
    boolean loginSuccess;
    String information;

    public LoginResponse(LoginInfo loginRequest, boolean loginSuccess, String information) {
        this.loginRequest = loginRequest;
        this.loginSuccess = loginSuccess;
        this.information = information;
    }

    public LoginInfo getLoginRequest() {
        return loginRequest;
    }

    public boolean isLoginSuccess() {
        return loginSuccess;
    }

    public String getInformation() {
        return information;
    }
}
