package io.github.shamrice.neChat.testClient.web.services.requests.messages;

import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.testClient.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.testClient.web.services.requests.RequestsBase;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Erik on 11/12/2017.
 */
public class MessagesRequests extends RequestsBase {

    public MessagesRequests(UserCredentials userCredentials, ClientConfiguration clientConfiguration) {
        super(userCredentials, clientConfiguration);
    }

    public MessagesResponse getMessages() {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("token", userCredentials.getAuthToken());

        JSONObject response = executeRequest(
                "GET",
                "messages",
                requestHeaders
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
