package io.github.shamrice.neChat.web.services.credentials;

/**
 * Created by Erik on 11/12/2017.
 */
public class UserCredentials {

    private String login;
    private String password;
    private String authToken;

    public UserCredentials() {}

    public UserCredentials(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserCredentials(String login, String password, String authToken) {
        this.login = login;
        this.password = password;
        this.authToken = authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
