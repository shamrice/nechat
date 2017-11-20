package io.github.shamrice.nechat.webservice.controller;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.dto.TokenDto;
import io.github.shamrice.nechat.core.db.dto.UserDto;
import io.github.shamrice.nechat.core.db.TokenAuthService;
import io.github.shamrice.nechat.core.db.UserService;
import io.github.shamrice.nechat.webservice.model.Greeting;
import io.github.shamrice.nechat.webservice.security.util.AuthAccessUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Erik on 10/18/2017.
 */

@RestController
@RequestMapping(value = "/")
public class AuthenticationController {
/*
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(
            @RequestParam(value = "name", defaultValue = "World") String name) {

        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }
*/
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
        throw new AccessDeniedException("Invalid token.");
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

            if (token == null) {
                System.out.println("token was null. Creating new token");
                tokenAuthService.createToken(user.getUserId());
                System.out.println("created token");
                token = tokenAuthService.getToken(user.getUserId());
                System.out.println("Token is now " + token + ". returning that to user.");
            }

            return token;
        }

        return null;
    }


}
