package io.github.shamrice.neChat.web.services.requests.messages;

import io.github.shamrice.neChat.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.web.services.requests.RequestsBase;
import io.github.shamrice.neChat.web.services.requests.StatusResponse;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 11/12/2017.
 */
public class MessagesRequests extends RequestsBase {

    public MessagesRequests(UserCredentials userCredentials, ClientConfiguration clientConfiguration) {
        super(userCredentials, clientConfiguration);
    }

    public StatusResponse sendMessage(String buddyLogin, String message) {

        JSONObject response = executeRequest(
                "POST",
                "messages/" + buddyLogin + "/",
                getTokenRequestHeader(),
                message
        );

        return new StatusResponse(
                response.getString("status"),
                response.getString("message")
        );
    }

    public MessagesResponse getMessages() {

        JSONObject response = executeRequest(
                "GET",
                "messages",
                getTokenRequestHeader()
        );

        List<Message> messageList = new ArrayList<>();

        for (Object responseObj : response.getJSONArray("messageDtos")) {
            JSONObject messageObj = (JSONObject) responseObj;
            Message message = new Message(
                    messageObj.getInt("id"),
                    messageObj.getInt("userId"),
                    messageObj.getString("login"),
                    messageObj.getInt("fromUserId"),
                    messageObj.getString("fromLogin"),
                    messageObj.getString("message"),
                    new Date(messageObj.getInt("createDate")), //does not work!
                    messageObj.getBoolean("read")
            );
            messageList.add(message);
        }

        return new MessagesResponse(
                response.getString("login"),
                messageList
        );
    }

    public MessagesResponse getChronologicalMessageHistory(String withLogin) {
        JSONObject response = executeRequest(
                "GET",
                "messages/" + withLogin,
                getTokenRequestHeader()
        );

        List<Message> messageList = new ArrayList<>();

        for(Object responseObj : response.getJSONArray("messageDtos")) {
            JSONObject messageObj = (JSONObject)responseObj;
            Message message = new Message(
                    messageObj.getInt("id"),
                    messageObj.getInt("userId"),
                    messageObj.getString("login"),
                    messageObj.getInt("fromUserId"),
                    messageObj.getString("fromLogin"),
                    messageObj.getString("message"),
                    new Date(messageObj.getInt("createDate")), //does not work!
                    messageObj.getBoolean("read")
            );
            messageList.add(message);
        }

        return new MessagesResponse(
                response.getString("login"),
                messageList
        );
    }

}
