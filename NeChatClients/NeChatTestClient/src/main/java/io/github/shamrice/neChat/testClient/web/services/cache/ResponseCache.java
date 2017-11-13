package io.github.shamrice.neChat.testClient.web.services.cache;

import io.github.shamrice.neChat.testClient.web.services.requests.buddies.Buddy;
import io.github.shamrice.neChat.testClient.web.services.requests.messages.Message;

import java.util.List;

/**
 * Created by Erik on 11/13/2017.
 *
 * Cache is used by client app when data from a previous response is needed but a new rest call is not necessary.
 */
public class ResponseCache {

    private List<Message> messageList;
    private List<Buddy> buddyList;

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setBuddyList(List<Buddy> buddyList) {
        this.buddyList = buddyList;
    }

    public List<Buddy> getBuddyList() {
        return buddyList;
    }
}
