package io.github.shamrice.neChat.testClient.web.services.requests.buddies;

/**
 * Created by Erik on 11/13/2017.
 */
public class Buddy {
    public int userId;
    public String login;

    public Buddy(int userId, String login) {
        this.userId = userId;
        this.login = login;
    }

    public int getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

}
