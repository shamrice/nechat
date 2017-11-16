package io.github.shamrice.neChat.web.services.requests.messages;

import java.util.Date;

/**
 * Created by Erik on 11/13/2017.
 */
public class Message {

    private int id;
    private int userId;
    private String login;
    private int fromUserId;
    private String fromLogin;
    private String message;
    private Date createDate;
    private boolean read;

    public Message(int id, int userId, String login, int fromUserId, String fromLogin, String message, Date createDate, boolean read) {
        this.id = id;
        this.userId = userId;
        this.login = login;
        this.fromUserId = fromUserId;
        this.fromLogin = fromLogin;
        this.message = message;
        this.createDate = createDate;
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public String getFromLogin() {
        return fromLogin;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public boolean isRead() {
        return read;
    }

}
