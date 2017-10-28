package io.github.shamrice.nechat.core.db.dto;


import java.util.Date;

/**
 * Created by Erik on 10/20/2017.
 */
public class MessageDto {

    private int id;
    private int userId;
    private String login;
    private int fromUserId;
    private String fromLogin;
    private String message;
    private boolean isRead;
    private Date createDate;

    public MessageDto(int id, int userId, String login, int fromUserId, String fromLogin, String message, boolean isRead, Date createDate) {
        this.id = id;
        this.userId = userId;
        this.login = login;
        this.fromUserId = fromUserId;
        this.fromLogin = fromLogin;
        this.message = message;
        this.isRead = isRead;
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
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

    public boolean isRead() {
        return isRead;
    }

    public Date getCreateDate() {
        return createDate;
    }
}
