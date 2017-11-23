package io.github.shamrice.nechat.core.db.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 10/28/2017.
 */
public class BuddiesDto extends BaseDto implements DbDto {

    private String login;
    private List<UserDto> buddies = new ArrayList<>();

    public BuddiesDto() {}

    public BuddiesDto(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setIdBuddies(List<UserDto> buddies) {
        this.buddies = buddies;
    }

    public List<UserDto> getBuddies() {
        return buddies;
    }
}
