package io.github.shamrice.nechat.webservice.controller;

import io.github.shamrice.nechat.core.CoreContext;
import io.github.shamrice.nechat.core.db.BuddiesService;
import io.github.shamrice.nechat.core.db.dto.BuddiesDto;
import io.github.shamrice.nechat.core.db.TokenAuthService;
import io.github.shamrice.nechat.webservice.response.Status;
import io.github.shamrice.nechat.webservice.response.StatusResponse;
import io.github.shamrice.nechat.webservice.security.util.AuthAccessUtil;
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
            throw new AccessDeniedException("Unable to authenticate using token.");
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
                return new StatusResponse(Status.FAILURE, "Buddy exists");
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
