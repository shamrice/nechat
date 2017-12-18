package io.github.shamrice.nechat.server.web.service.controller;

import io.github.shamrice.nechat.server.core.CoreContext;
import io.github.shamrice.nechat.server.core.db.dto.TokenDto;
import io.github.shamrice.nechat.server.core.db.dto.UserDto;
import io.github.shamrice.nechat.server.core.db.TokenAuthService;
import io.github.shamrice.nechat.server.core.db.UserService;
import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;
import io.github.shamrice.nechat.server.web.service.security.util.AuthAccessUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Erik on 10/18/2017.
 */

@RestController
@RequestMapping(value = "/")
public class AuthenticationController {

    @RequestMapping(value = "/auth/user", method = RequestMethod.GET)
    public UserDto getUser(
            @RequestHeader(value = "token") String token,
            @RequestParam(value = "login", defaultValue = "") String login
    ) throws AccessDeniedException {

        String currentUser = AuthAccessUtil.getCurrentLoginPrincipal();
        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());

        if (tokenAuthService.authorizeToken(token, currentUser)) {
            UserService userService = new UserService(CoreContext.getInstance());
            return userService.getUser(login);
        }

        Log.get().logMessage(LogLevel.INFORMATION, this.getClass().getSimpleName() + ": " +
                "Unable to authenticate using token: " + token);
        throw new AccessDeniedException("Unable to authenticate user "
                + currentUser + " with token " + token);

    }

    @RequestMapping(value = "/auth/token/{login}", method = RequestMethod.GET)
    public @ResponseBody
    TokenDto getToken(
            @PathVariable("login") String login
    ) throws AccessDeniedException {

        AuthAccessUtil.verifyLoginMatchesAuth(login);

        UserService userService = new UserService(CoreContext.getInstance());
        UserDto user = userService.getUser(login);

        if (user != null) {
            TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());

            TokenDto token = tokenAuthService.getToken(user.getUserId());

            if (token.getAuthToken() == null || token.getAuthToken().isEmpty()) {
                Log.get().logMessage(LogLevel.INFORMATION, this.getClass().getSimpleName() + ": " +
                        "token was null. Creating new token");

                tokenAuthService.createToken(user.getUserId());
                Log.get().logMessage(LogLevel.INFORMATION, this.getClass().getSimpleName() + ": " +
                        "Created token");

                token = tokenAuthService.getToken(user.getUserId());

            }

            Log.get().logMessage(LogLevel.INFORMATION, this.getClass().getSimpleName() + ": " +
                    "getTokens - Returning token " + token.getAuthToken() + ". to " + login + "."
            );

            return token;
        }

        throw new AccessDeniedException("Unable to get tokens for user " + login);
    }


}
