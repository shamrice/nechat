package io.github.shamrice.nechat.server.web.service.controller;

import io.github.shamrice.nechat.server.core.CoreContext;
import io.github.shamrice.nechat.server.core.db.MessageService;
import io.github.shamrice.nechat.server.core.db.TokenAuthService;
import io.github.shamrice.nechat.server.core.db.dto.MessagesDto;
import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;
import io.github.shamrice.nechat.server.web.service.response.Status;
import io.github.shamrice.nechat.server.web.service.response.StatusResponse;
import io.github.shamrice.nechat.server.web.service.security.util.AuthAccessUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

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
        new TokenAuthService(CoreContext.getInstance()).authorizeToken(token, login);

        MessageService messageService = new MessageService(CoreContext.getInstance());
        messages = messageService.getUnreadMessages(login);

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
        new TokenAuthService(CoreContext.getInstance()).authorizeToken(token, fromLogin);

        MessageService messageService = new MessageService(CoreContext.getInstance());
        if (messageService.sendMessageToUser(fromLogin, toLogin, body)) {
            return new StatusResponse(Status.SUCCESS, "Message sent");
        } else {
            return new StatusResponse(Status.FAILURE, "Message failed");
        }
    }

    @RequestMapping(value = "messages/history/{login}", method = RequestMethod.GET)
    public @ResponseBody
    MessagesDto getChronologicalMessageHistoryWithUser(
            @RequestHeader(value = "token", required =  true) String token,
            @PathVariable(value = "login") String withLogin,
            @RequestParam(value = "after", required = false) String afterMessageId
    ) {
        MessagesDto messagesDto = null;

        String currentLogin = AuthAccessUtil.getCurrentLoginPrincipal();
        new TokenAuthService(CoreContext.getInstance()).authorizeToken(token, currentLogin);

        int startMessageId = 0;
        if (afterMessageId != null && !afterMessageId.isEmpty()) {
            try {
                startMessageId = Integer.parseInt(afterMessageId);
            } catch (NumberFormatException formatExc) {
                Log.get().logExceptionWithMessage(
                        this.getClass().getSimpleName() +
                                ": " + afterMessageId + " is not a valid number. Setting to 0.",
                        formatExc
                );
            }
        }

        MessageService messageService = new MessageService(CoreContext.getInstance());
        messagesDto = messageService.getChronologicalMessageHistory(currentLogin, withLogin, startMessageId);

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
        new TokenAuthService(CoreContext.getInstance()).authorizeToken(token, currentLogin);

        MessageService messageService = new MessageService(CoreContext.getInstance());
        messagesDto = messageService.getUnreadMessagesWithUser(currentLogin, withLogin);
        return messagesDto;
    }

    @RequestMapping(value = "messages/read", method = RequestMethod.POST)
    public @ResponseBody
    StatusResponse markMessagesAsRead(
            @RequestHeader(value = "token", required = true) String token,
            @RequestBody String body
    ) {

        String currentLogin = AuthAccessUtil.getCurrentLoginPrincipal();
        new TokenAuthService(CoreContext.getInstance()).authorizeToken(token, currentLogin);

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
}
