package io.github.shamrice.neChat.application.rest.client.requests.messages;

import io.github.shamrice.neChat.application.rest.client.requests.StatusResponse;
import io.github.shamrice.neChat.application.rest.client.configuration.ClientConfiguration;
import io.github.shamrice.neChat.application.rest.client.credentials.UserCredentials;
import io.github.shamrice.neChat.application.rest.client.requests.RequestsBase;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
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

        if (response != null) {
            return new StatusResponse(
                    response.getString("status"),
                    response.getString("message")
            );
        } else {
            return  new StatusResponse("failure", "No response was returned from server.");
        }
    }

    public StatusResponse markMessagesAsRead(List<Message> messages) {

        StringBuilder sb = new StringBuilder();
        for (Message message : messages) {
            sb.append(message.getId());
            sb.append(",");
        }

        JSONObject response = executeRequest(
                "POST",
                "messages/read",
                getTokenRequestHeader(),
                sb.toString()
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
                    new Date(messageObj.getLong("createDate")),
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
        return getMessageHistoryAfterMessageId(withLogin, 0);
    }

    public MessagesResponse getMessageHistoryAfterMessageId(String withLogin, int afterMessageId) {

        String resourceLocation = "messages/history/" + withLogin;
        if (afterMessageId > 0) {
            resourceLocation += "?after=" + afterMessageId;
        }

        JSONObject response = executeRequest(
                "GET",
                resourceLocation,
                getTokenRequestHeader()
        );

        List<Message> messageList = new ArrayList<>();

        if (response.getJSONArray("messageDtos") != null) {
            for (Object responseObj : response.getJSONArray("messageDtos")) {
                JSONObject messageObj = (JSONObject) responseObj;
                Message message = new Message(
                        messageObj.getInt("id"),
                        messageObj.getInt("userId"),
                        messageObj.getString("login"),
                        messageObj.getInt("fromUserId"),
                        messageObj.getString("fromLogin"),
                        messageObj.getString("message"),
                        new Date(messageObj.getLong("createDate")),
                        messageObj.getBoolean("read")
                );
                messageList.add(message);
            }
        }

        return new MessagesResponse(
                response.getString("login"),
                messageList
        );
    }

    public MessagesResponse getUnreadMessagesWithUser(String withLogin) {
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
                    new Date(messageObj.getLong("createDate")),
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
