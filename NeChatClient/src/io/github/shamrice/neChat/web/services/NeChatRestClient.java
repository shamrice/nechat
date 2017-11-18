package io.github.shamrice.neChat.web.services;

import io.github.shamrice.neChat.web.services.cache.ResponseCache;
import io.github.shamrice.neChat.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.web.services.requests.Response;
import io.github.shamrice.neChat.web.services.requests.StatusResponse;
import io.github.shamrice.neChat.web.services.requests.authorization.AuthorizationRequests;
import io.github.shamrice.neChat.web.services.requests.authorization.AuthorizationResponse;
import io.github.shamrice.neChat.web.services.requests.buddies.BuddiesRequests;
import io.github.shamrice.neChat.web.services.requests.messages.Message;
import io.github.shamrice.neChat.web.services.requests.messages.MessagesRequests;
import io.github.shamrice.neChat.web.services.requests.buddies.BuddiesResponse;
import io.github.shamrice.neChat.web.services.requests.messages.MessagesResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 10/30/2017.
 */
public class NeChatRestClient {

    private ClientConfiguration clientConfiguration;
    private UserCredentials userCredentials;
    private ResponseCache responseCache;

    public NeChatRestClient(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
        this.userCredentials = clientConfiguration.getUserCredentials(); //new UserCredentials();
        this.responseCache = new ResponseCache();
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
    }

    public Response getAuthToken() {
        AuthorizationResponse response = new AuthorizationRequests(userCredentials, clientConfiguration).getAuthToken();
        userCredentials.setAuthToken(response.getAuthToken());
        return response;
    }

    public Response addBuddy(String buddyLogin) {
        return new BuddiesRequests(userCredentials, clientConfiguration).addBuddy(buddyLogin);
    }

    public Response removeBuddy(String buddyLogin) {
        return new BuddiesRequests(userCredentials, clientConfiguration).removeBuddy(buddyLogin);
    }

    public Response getBuddies() {
        BuddiesResponse response =  new BuddiesRequests(userCredentials, clientConfiguration).getBuddies();
        responseCache.setBuddyList(response.getBuddyList());
        return response;
    }

    public Response getMessages() {
        MessagesResponse response = new MessagesRequests(userCredentials, clientConfiguration).getMessages();
        responseCache.setMessageList(response.getMessageList());
        return response;
    }

    public Response getMessagesWithUser(String withLogin) {
        if (responseCache.getUserMessages(withLogin) == null) {
            return getChronologicalMessageHistoryWithUser(withLogin);
        } else {
            return getUnreadMessagesWithUser(withLogin);
        }
    }

    public Response sendMessage(String buddyLogin, String message) {
        return new MessagesRequests(userCredentials, clientConfiguration).sendMessage(buddyLogin, message);
    }

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
        MessagesResponse response = new MessagesRequests(userCredentials, clientConfiguration)
                .getUnreadMessagesWithUser(withLogin);

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
