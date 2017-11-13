package io.github.shamrice.neChat.testClient.web.services.requests.buddies;

import java.util.List;

/**
 * Created by Erik on 11/13/2017.
 */
public class BuddiesResponse {

    private String login;
    private List<Buddy> buddyList;

    public BuddiesResponse(String login, List<Buddy> buddyList) {
        this.login = login;
        this.buddyList = buddyList;
    }

    public String getLogin() {
        return login;
    }

    public List<Buddy> getBuddyList() {
        return buddyList;
    }

    public Buddy getBuddy(int userId) {
        Buddy result = null;

        for (Buddy buddy : buddyList) {
            if (buddy.getUserId() == userId) {
                result = buddy;
                break;
            }
        }
        return result;
    }

    public Buddy getBuddy(String login) {
        Buddy result = null;

        for (Buddy buddy : buddyList) {
            if (buddy.getLogin().equals(login)) {
                result = buddy;
                break;
            }
        }
        return result;
    }

}
