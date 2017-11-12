package io.github.shamrice.neChat.testClient.web.services.requests;

import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.testClient.web.services.credentials.UserCredentials;
import org.json.JSONObject;

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

    public String getMessages() {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("token", userCredentials.getAuthToken());

        return executeRequest(
                "GET",
                "messages",
                requestHeaders).toString();

    }
}
