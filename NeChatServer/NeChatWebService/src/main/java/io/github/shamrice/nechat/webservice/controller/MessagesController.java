package io.github.shamrice.nechat.webservice.controller;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.dto.MessageDto;
import io.github.shamrice.nechat.core.db.MessageService;
import io.github.shamrice.nechat.core.db.TokenAuthService;
import io.github.shamrice.nechat.core.db.dto.MessagesDto;
import io.github.shamrice.nechat.webservice.security.util.AuthAccessUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Erik on 10/20/2017.
 */

@RestController
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
    public @ResponseBody HttpStatus sendMessageToUser(
            @RequestHeader(value = "token", required = true) String token,
            @RequestBody String body,
            @PathVariable(value = "login") String toLogin
    ) {

        String fromLogin = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, fromLogin)) {
            MessageService messageService = new MessageService(CoreContext.getInstance());
            if (messageService.sendMessageToUser(fromLogin, toLogin, body)) {
                return HttpStatus.CREATED;
            }
        }

        return HttpStatus.FORBIDDEN;
    }
}
