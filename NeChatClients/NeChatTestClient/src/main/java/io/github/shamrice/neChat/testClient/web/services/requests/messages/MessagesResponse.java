package io.github.shamrice.neChat.testClient.web.services.requests.messages;

import java.util.List;

/**
 * Created by Erik on 11/13/2017.
 */
public class MessagesResponse {

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
