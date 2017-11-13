package io.github.shamrice.neChat.testClient.web.services;

import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.testClient.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.testClient.web.services.requests.StatusResponse;
import io.github.shamrice.neChat.testClient.web.services.requests.authorization.AuthorizationRequests;
import io.github.shamrice.neChat.testClient.web.services.requests.authorization.AuthorizationResponse;
import io.github.shamrice.neChat.testClient.web.services.requests.buddies.BuddiesRequests;
import io.github.shamrice.neChat.testClient.web.services.requests.messages.MessagesRequests;
import io.github.shamrice.neChat.testClient.web.services.requests.buddies.BuddiesResponse;
import io.github.shamrice.neChat.testClient.web.services.requests.messages.MessagesResponse;

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

    public AuthorizationResponse getAuthToken() {
        AuthorizationResponse response = new AuthorizationRequests(userCredentials, clientConfiguration).getAuthToken();
        userCredentials.setAuthToken(response.getAuthToken());
        return response;
    }

    public StatusResponse addBuddy(String buddyLogin) {
        return new BuddiesRequests(userCredentials, clientConfiguration).addBuddy(buddyLogin);
    }

    public BuddiesResponse getBuddies() {
        return new BuddiesRequests(userCredentials, clientConfiguration).getBuddies();
    }

    public MessagesResponse getMessages() {
        return new MessagesRequests(userCredentials, clientConfiguration).getMessages();
    }

}
