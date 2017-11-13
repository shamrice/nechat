package io.github.shamrice.neChat.testClient.web.services.requests.authorization;

import io.github.shamrice.neChat.testClient.web.services.requests.Response;

import java.util.Date;

/**
 * Created by Erik on 11/13/2017.
 */
public class AuthorizationResponse implements Response {
    private int tokenAuthId;
    private int userId;
    private String authToken;
    private Date create;
    private Date expire;

    public AuthorizationResponse(int tokenAuthId, int userId, String authToken, Date create, Date expire) {
        this.tokenAuthId = tokenAuthId;
        this.userId = userId;
        this.authToken = authToken;
        this.create = create;
        this.expire = expire;
    }

    public AuthorizationResponse(String authToken, Date create, Date expire) {
        this.authToken = authToken;
        this.create = create;
        this.expire = expire;
    }

    public int getTokenAuthId() {
        return tokenAuthId;
    }

    public int getUserId() {
        return userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Date getCreate() {
        return create;
    }

    public Date getExpire() {
        return expire;
    }
}
