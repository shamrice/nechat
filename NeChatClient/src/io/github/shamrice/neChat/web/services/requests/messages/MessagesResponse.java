package io.github.shamrice.neChat.web.services.requests.messages;

import io.github.shamrice.neChat.web.services.requests.Response;

import java.util.List;

/**
 * Created by Erik on 11/13/2017.
 */
public class MessagesResponse implements Response {

    private String login;
    private List<Message> messageList;

    public MessagesResponse(String login, List<Message> messages) {
        this.login = login;
        this.messageList = messages;
    }

    public String getLogin() {
        return login;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

}
