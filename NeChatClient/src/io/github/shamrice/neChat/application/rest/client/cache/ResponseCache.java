package io.github.shamrice.neChat.application.rest.client.cache;

import io.github.shamrice.neChat.application.rest.client.cache.conversation.Conversation;
import io.github.shamrice.neChat.application.rest.client.requests.buddies.Buddy;
import io.github.shamrice.neChat.application.rest.client.requests.messages.Message;

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

    //private List<Message> messageList = new ArrayList<>();
    private Map<String, List<Conversation>> userMessageList = new HashMap<>();
    private List<Buddy> buddyList = new ArrayList<>();
/*
    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public List<Message> getMessageList() {
        return messageList;
    }
*/
    public void setBuddyList(List<Buddy> buddyList) {
        this.buddyList = buddyList;
    }

    public List<Buddy> getBuddyList() {
        return buddyList;
    }

    public void setUserMessages(String login, List<Message> messages) {

        List<Conversation> conversationList = new ArrayList<>();

        for (Message message : messages) {
            conversationList.add(new Conversation(message.getFromLogin(), message));
        }

        userMessageList.put(login, conversationList);
    }

    public List<Message> getUserMessages(String login) {

        if (userMessageList.get(login) != null) {
            List<Message> resultSet = new ArrayList<>();
            for (Conversation conversation : userMessageList.get(login)) {
                resultSet.add(conversation.getMessage());
            }

            return resultSet;
        }

        return null;
    }

    public void addUserMessages(String login, List<Message> messages) {
        for (Message message : messages) {
            userMessageList.get(login).add(new Conversation(message.getFromLogin(), message));
        }
    }

    public void addUserMessage(String login, Message message) {
        if (userMessageList.get(login) != null) {
            userMessageList.get(login).add(new Conversation(message.getFromLogin(), message));
        } else {
            List<Conversation> newConversation = new ArrayList<>();
            newConversation.add(new Conversation(message.getFromLogin(), message));
            userMessageList.put(login, newConversation);
        }
    }
}
