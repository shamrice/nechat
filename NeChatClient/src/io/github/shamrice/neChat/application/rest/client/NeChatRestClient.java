package io.github.shamrice.neChat.application.rest.client;

import io.github.shamrice.neChat.application.rest.client.cache.ResponseCache;
import io.github.shamrice.neChat.application.rest.client.credentials.UserCredentials;
import io.github.shamrice.neChat.application.rest.client.requests.Response;
import io.github.shamrice.neChat.application.rest.client.requests.StatusResponse;
import io.github.shamrice.neChat.application.rest.client.requests.authorization.AuthorizationRequests;
import io.github.shamrice.neChat.application.rest.client.requests.authorization.AuthorizationResponse;
import io.github.shamrice.neChat.application.rest.client.requests.buddies.BuddiesRequests;
import io.github.shamrice.neChat.application.rest.client.requests.buddies.BuddiesResponse;
import io.github.shamrice.neChat.application.rest.client.requests.messages.Message;
import io.github.shamrice.neChat.application.rest.client.requests.messages.MessagesRequests;

import java.util.Date;

/**
 * Created by Erik on 12/10/2017.
 */
public interface NeChatRestClient {
    void setUserCredentials(UserCredentials userCredentials);

    String getCurrentLogin();

    Response getAuthToken();

    Response addBuddy(String buddyLogin);

    Response removeBuddy(String buddyLogin);

    Response getBuddies();

    Response getMessagesWithUser(String withLogin);

    Response sendMessage(String buddyLogin, String message);

    ResponseCache getResponseCache();


}
