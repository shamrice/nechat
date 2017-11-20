package io.github.shamrice.nechat.webservice.controller;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.dto.MessageDto;
import io.github.shamrice.nechat.core.db.MessageService;
import io.github.shamrice.nechat.core.db.TokenAuthService;
import io.github.shamrice.nechat.core.db.dto.MessagesDto;
import io.github.shamrice.nechat.webservice.response.Status;
import io.github.shamrice.nechat.webservice.response.StatusResponse;
import io.github.shamrice.nechat.webservice.security.util.AuthAccessUtil;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Erik on 10/20/2017.
 */

@RestController
@RequestMapping(value = "/")
public class MessagesController {

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public @ResponseBody MessagesDto getMessagesForUser(
            @RequestHeader(value = "token", required = true) String token
    ) throws AccessDeniedException {
        MessagesDto messages = null;

        String login = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, login)) {
            MessageService messageService = new MessageService(CoreContext.getInstance());
            messages = messageService.getUnreadMessages(login);
        } else {
            throw new AccessDeniedException("Unable to authenticate using token.");
        }

        return messages;
    }

    @RequestMapping(value = "messages/{login}", method = RequestMethod.POST)
    public @ResponseBody
    StatusResponse sendMessageToUser(
            @RequestHeader(value = "token", required = true) String token,
            @RequestBody String body,
            @PathVariable(value = "login") String toLogin
    ) {

        String fromLogin = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, fromLogin)) {
            MessageService messageService = new MessageService(CoreContext.getInstance());
            if (messageService.sendMessageToUser(fromLogin, toLogin, body)) {
                return new StatusResponse(Status.SUCCESS, "Message sent");
            } else {
                return new StatusResponse(Status.FAILURE, "Message failed");
            }
        }

        return new StatusResponse(Status.INVALID, "FORBIDDEN");
    }

    @RequestMapping(value = "messages/history/{login}", method = RequestMethod.GET)
    public @ResponseBody
    MessagesDto getChronologicalMessageHistoryWithUser(
            @RequestHeader(value = "token", required =  true) String token,
            @PathVariable(value = "login") String withLogin
    ) {
        MessagesDto messagesDto = null;
        String currentLogin = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, currentLogin)) {
            MessageService messageService = new MessageService(CoreContext.getInstance());
            messagesDto = messageService.getChronologicalMessageHistory(currentLogin, withLogin);
        } else {
            throw new AccessDeniedException("Unable to authenticate token.");
        }

        return messagesDto;
    }

    @RequestMapping(value = "messages/{login}", method = RequestMethod.GET)
    public @ResponseBody
    MessagesDto getUnreadMessagesWithUser(
            @RequestHeader(value = "token", required =  true) String token,
            @PathVariable(value = "login") String withLogin
    ) {
        MessagesDto messagesDto = null;
        String currentLogin = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, currentLogin)) {
            MessageService messageService = new MessageService(CoreContext.getInstance());
            messagesDto = messageService.getUnreadMessagesWithUser(currentLogin, withLogin);
        } else {
            throw new AccessDeniedException("Unable to authenticate token.");
        }

        return messagesDto;
    }

    @RequestMapping(value = "messages/read", method = RequestMethod.POST)
    public @ResponseBody
    StatusResponse markMessagesAsRead(
            @RequestHeader(value = "token", required = true) String token,
            @RequestBody String body
    ) {

        String currentLogin = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, currentLogin)) {
            MessageService messageService = new MessageService(CoreContext.getInstance());

            String[] messageIdStrings = body.split(",");
            int messageIds[] = new int[messageIdStrings.length];
            for (int i = 0; i < messageIdStrings.length; i++) {
                messageIds[i] = Integer.parseInt(messageIdStrings[i]);
            }

            if (messageService.markMessagesAsRead(currentLogin, messageIds)) {
                return new StatusResponse(Status.SUCCESS, "Message(s) marked as read.");
            } else {
                return new StatusResponse(Status.FAILURE, "Message(s) failed to be marked as read");
            }
        }

        return new StatusResponse(Status.INVALID, "FORBIDDEN");
    }
}
