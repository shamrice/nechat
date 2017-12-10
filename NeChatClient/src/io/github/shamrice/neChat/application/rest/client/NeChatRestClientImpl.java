package io.github.shamrice.neChat.application.rest.client;

import io.github.shamrice.neChat.application.rest.client.cache.ResponseCache;
import io.github.shamrice.neChat.application.rest.client.configuration.ClientConfiguration;
import io.github.shamrice.neChat.application.rest.client.credentials.UserCredentials;
import io.github.shamrice.neChat.application.rest.client.requests.Response;
import io.github.shamrice.neChat.application.rest.client.requests.StatusResponse;
import io.github.shamrice.neChat.application.rest.client.requests.authorization.AuthorizationRequests;
import io.github.shamrice.neChat.application.rest.client.requests.authorization.AuthorizationResponse;
import io.github.shamrice.neChat.application.rest.client.requests.buddies.BuddiesRequests;
import io.github.shamrice.neChat.application.rest.client.requests.buddies.BuddiesResponse;
import io.github.shamrice.neChat.application.rest.client.requests.messages.Message;
import io.github.shamrice.neChat.application.rest.client.requests.messages.MessagesRequests;
import io.github.shamrice.neChat.application.rest.client.requests.messages.MessagesResponse;

import java.util.Date;
import java.util.List;

/**
 * Created by Erik on 10/30/2017.
 */
public class NeChatRestClientImpl implements NeChatRestClient {

    private ClientConfiguration clientConfiguration;
    private UserCredentials userCredentials;
    private ResponseCache responseCache;

    public NeChatRestClientImpl(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        this.userCredentials = clientConfiguration.getUserCredentials(); //new UserCredentials();
        this.responseCache = new ResponseCache();
    }

    @Override
    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    @Override
    public String getCurrentLogin() {
        if (userCredentials != null) {
            return userCredentials.getLogin();
        } else {
            return null;
        }
    }

    @Override
    public Response getAuthToken() {
        AuthorizationResponse response = new AuthorizationRequests(userCredentials, clientConfiguration).getAuthToken();
        userCredentials.setAuthToken(response.getAuthToken());
        return response;
    }

    @Override
    public Response addBuddy(String buddyLogin) {
        return new BuddiesRequests(userCredentials, clientConfiguration).addBuddy(buddyLogin);
    }

    @Override
    public Response removeBuddy(String buddyLogin) {
        return new BuddiesRequests(userCredentials, clientConfiguration).removeBuddy(buddyLogin);
    }

    @Override
    public Response getBuddies() {
        BuddiesResponse response =  new BuddiesRequests(userCredentials, clientConfiguration).getBuddies();
        responseCache.setBuddyList(response.getBuddyList());
        return response;
    }
/*
    public Response getMessages() {
        MessagesResponse response = new MessagesRequests(userCredentials, clientConfiguration).getMessages();
        responseCache.setMessageList(response.getMessageList());
        return response;
    }
*/

    @Override
    public Response getMessagesWithUser(String withLogin) {
        if (responseCache.getUserMessages(withLogin) == null) {
            return getChronologicalMessageHistoryWithUser(withLogin);
        } else {
            return getUnreadMessagesWithUser(withLogin);
        }
    }

    @Override
    public Response sendMessage(String buddyLogin, String message) {
        StatusResponse response = new MessagesRequests(userCredentials, clientConfiguration).sendMessage(buddyLogin, message);


        if (response.isSuccess()) {
            Message newMessageSent = new Message(-1, -1, userCredentials.getLogin(), -2, buddyLogin, message, new Date(), true);
            responseCache.addUserMessage(userCredentials.getLogin(), newMessageSent);
        }

        return response;
    }

    @Override
    public ResponseCache getResponseCache() {
        return responseCache;
    }


    private MessagesResponse getChronologicalMessageHistoryWithUser(String withLogin) {
        MessagesResponse response = new MessagesRequests(userCredentials, clientConfiguration)
                .getChronologicalMessageHistory(withLogin);

        if (response.getMessageList().size() > 0) {
            responseCache.setUserMessages(withLogin, response.getMessageList());
        }
        return response;
    }

    private MessagesResponse getUnreadMessagesWithUser(String withLogin) {
        /*
        MessagesResponse response = new MessagesRequests(userCredentials, clientConfiguration)
                .getUnreadMessagesWithUser(withLogin);
        */

        //woo this is some fun nested method calls....
        MessagesResponse response = new MessagesRequests(userCredentials, clientConfiguration)
                .getMessageHistoryAfterMessageId(
                        withLogin,
                        responseCache.getUserMessages(withLogin)
                                .get(responseCache.getUserMessages(withLogin).size() - 1)
                                .getId()
                );

        //add unread messages to cache.
        if (response.getMessageList().size() > 0) {
            responseCache.addUserMessages(withLogin, response.getMessageList());
            StatusResponse markAsRead = markMessagesAsRead(response.getMessageList());
            if (!markAsRead.isSuccess()) {
                System.out.println(markAsRead.getStatus() + " - " + markAsRead.getMessage());
            }
        }

        //return messages from cache for user.
        return  new MessagesResponse(userCredentials.getLogin(), getResponseCache().getUserMessages(withLogin));
    }

    private StatusResponse markMessagesAsRead(List<Message> messages) {
        return new MessagesRequests(userCredentials, clientConfiguration).markMessagesAsRead(messages);
    }


}
