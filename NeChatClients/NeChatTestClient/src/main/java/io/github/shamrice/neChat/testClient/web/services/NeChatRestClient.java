package io.github.shamrice.neChat.testClient.web.services;

import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.testClient.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.testClient.web.services.requests.AuthorizationRequests;
import io.github.shamrice.neChat.testClient.web.services.requests.BuddiesRequests;
import io.github.shamrice.neChat.testClient.web.services.requests.MessagesRequests;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Erik on 10/30/2017.
 */
public class NeChatRestClient {

    private ClientConfiguration clientConfiguration;
    private UserCredentials userCredentials;

    public NeChatRestClient(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        this.userCredentials = new UserCredentials();
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public String getAuthToken() {
        userCredentials.setAuthToken(
                new AuthorizationRequests(userCredentials, clientConfiguration).getAuthToken()
        );
        return userCredentials.getAuthToken();
    }

    public String getBuddies() {
        return new BuddiesRequests(userCredentials, clientConfiguration).getBuddies();
    }

    public String getMessages() {
        return new MessagesRequests(userCredentials, clientConfiguration).getMessages();
    }

}
