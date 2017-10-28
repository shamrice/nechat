package io.github.shamrice.nechat.webservice.security.util;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Created by Erik on 10/28/2017.
 */
public class AuthAccessUtil {

    public static void verifyLoginMatchesAuth(String login) throws AccessDeniedException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (!login.equals(currentPrincipalName))
            throw new AccessDeniedException("Access denied.");
    }

    public static String getCurrentLoginPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
