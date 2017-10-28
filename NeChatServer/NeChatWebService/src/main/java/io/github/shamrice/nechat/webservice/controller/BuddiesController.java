package io.github.shamrice.nechat.webservice.controller;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.BuddiesService;
import io.github.shamrice.nechat.core.db.dto.BuddiesDto;
import io.github.shamrice.nechat.core.db.TokenAuthService;
import io.github.shamrice.nechat.webservice.security.util.AuthAccessUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Erik on 10/28/2017.
 */

@RestController
public class BuddiesController {

    @RequestMapping(value = "/buddies", method = RequestMethod.GET)
    public @ResponseBody
    BuddiesDto getBuddyList(
            @RequestHeader(value = "token", required = true) String token
    ) throws AccessDeniedException {
        BuddiesDto buddiesDto = null;

        String login = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, login)) {
            BuddiesService buddiesService = new BuddiesService(CoreContext.getInstance());
            buddiesDto = buddiesService.getBuddyList(login);
        } else {
            throw new AccessDeniedException("Unable to authenticate using token.");
        }

        return buddiesDto;
    }

    @RequestMapping(value = "buddies/{buddy}", method = RequestMethod.PUT)
    public @ResponseBody
    HttpStatus addBuddy(
            @RequestHeader(value = "token", required = true) String token,
            @PathVariable(value = "buddy") String buddy
    ) {

        String login = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, login)) {
            BuddiesService buddiesService = new BuddiesService(CoreContext.getInstance());
            if (buddiesService.addBuddy(login, buddy)) {
                return HttpStatus.CREATED;
            } else {
                return HttpStatus.CONFLICT;
            }
        }

        return HttpStatus.FORBIDDEN;
    }

    @RequestMapping(value = "buddies/{buddy}", method = RequestMethod.DELETE)
    public @ResponseBody
    HttpStatus deleteBuddy(
            @RequestHeader(value = "token", required = true) String token,
            @PathVariable(value = "buddy") String buddy
    ) {

        String login = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, login)) {
            BuddiesService buddiesService = new BuddiesService(CoreContext.getInstance());
            if (buddiesService.deleteBuddy(buddy)) {
                return HttpStatus.OK;
            } else {
                return HttpStatus.NOT_FOUND;
            }
        }

        return HttpStatus.FORBIDDEN;
    }
}
