package io.github.shamrice.nechat.core.db.dto;

/**
 * Created by Erik on 10/19/2017.
 */
public class UserDto {

    private int userId;
    private String login;
    private String password;

    public UserDto(int userId, String login, String password) {
        this.userId = userId;
        this.login = login;
        this.password = password;
    }

    public UserDto(int userId, String login) {
        this.userId = userId;
        this.login = login;
        this.password = "";
    }

    public int getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
