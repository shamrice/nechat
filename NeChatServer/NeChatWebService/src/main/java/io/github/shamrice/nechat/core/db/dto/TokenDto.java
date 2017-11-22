package io.github.shamrice.nechat.core.db.dto;

import java.util.Date;

/**
 * Created by Erik on 10/20/2017.
 */
public class TokenDto  implements DbDto {

    private int tokenAuthId;
    private int userId;
    private String authToken;
    private Date create;
    private Date expire;

    public TokenDto(int tokenAuthId, int userId, String authToken, Date create, Date expire) {
        this.tokenAuthId = tokenAuthId;
        this.userId = userId;
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
