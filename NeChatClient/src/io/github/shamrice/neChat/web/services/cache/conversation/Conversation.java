package io.github.shamrice.neChat.web.services.cache.conversation;

import io.github.shamrice.neChat.web.services.requests.messages.Message;

/**
 * Created by Erik on 11/18/2017.
 */
public class Conversation {
    private String login;
    private Message message;

    public Conversation(String login, Message message) {
        this.login = login;
        this.message = message;
    }

    public String getLogin() {
        return login;
    }

    public Message getMessage() {
        return message;
    }
}
