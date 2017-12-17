package io.github.shamrice.nechat.server.web.service.controller;

import io.github.shamrice.nechat.server.core.CoreContext;
import io.github.shamrice.nechat.server.core.db.BuddiesService;
import io.github.shamrice.nechat.server.core.db.dto.BuddiesDto;
import io.github.shamrice.nechat.server.core.db.TokenAuthService;
import io.github.shamrice.nechat.server.logging.Log;
import io.github.shamrice.nechat.server.logging.LogLevel;
import io.github.shamrice.nechat.server.web.service.response.Status;
import io.github.shamrice.nechat.server.web.service.response.StatusResponse;
import io.github.shamrice.nechat.server.web.service.security.util.AuthAccessUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Erik on 10/28/2017.
 */

@RestController
@RequestMapping(value = "/")
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
            Log.get().logMessage(LogLevel.INFORMATION, this.getClass().getSimpleName() + ": " +
                    "Unable to authenticate using token: " + token);
        }

        return buddiesDto;
    }

    @RequestMapping(value = "buddies/{buddy}", method = RequestMethod.PUT)
    public @ResponseBody
    StatusResponse addBuddy(
            @RequestHeader(value = "token", required = true) String token,
            @PathVariable(value = "buddy") String buddy
    ) {

        String login = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, login)) {
            BuddiesService buddiesService = new BuddiesService(CoreContext.getInstance());
            if (buddiesService.addBuddy(login, buddy)) {
                return new StatusResponse(Status.SUCCESS, "Buddy added");
            } else {
                return new StatusResponse(Status.FAILURE, "Buddy is either already added or user does not exist.");
            }
        }

        return new StatusResponse(Status.INVALID, "FORBIDDEN");
    }

    @RequestMapping(value = "buddies/{buddy}", method = RequestMethod.DELETE)
    public @ResponseBody
    StatusResponse deleteBuddy(
            @RequestHeader(value = "token", required = true) String token,
            @PathVariable(value = "buddy") String buddy
    ) {

        String login = AuthAccessUtil.getCurrentLoginPrincipal();

        TokenAuthService tokenAuthService = new TokenAuthService(CoreContext.getInstance());
        if (tokenAuthService.authorizeToken(token, login)) {
            BuddiesService buddiesService = new BuddiesService(CoreContext.getInstance());
            if (buddiesService.deleteBuddy(buddy)) {
                return new StatusResponse(Status.SUCCESS, "Buddy deleted");
            } else {
                return new StatusResponse(Status.FAILURE, "Buddy not found.");
            }
        }

        return new StatusResponse(Status.INVALID, "FORBIDDEN");
    }
}