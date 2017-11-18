package io.github.shamrice.neChat.web.services.cache;

import io.github.shamrice.neChat.web.services.requests.buddies.Buddy;
import io.github.shamrice.neChat.web.services.requests.messages.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Erik on 11/13/2017.
 *
 * Cache is used by client app when data from a previous response is needed but a new rest call is not necessary.
 */
public class ResponseCache {

    private List<Message> messageList = new ArrayList<>();
    private Map<String, List<Message>> userMessageList = new HashMap<>();
    private List<Buddy> buddyList = new ArrayList<>();

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

    public void setUserMessages(String login, List<Message> messages) {
        userMessageList.put(login, messages);
    }

    public List<Message> getUserMessages(String login) {
        return userMessageList.get(login);
    }

    public void addUserMessages(String login, List<Message> messages) {
        for (Message message : messages) {
            userMessageList.get(login).add(message);
        }
    }
}
