package io.github.shamrice.nechat.server.web.service.security;


import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Erik on 10/20/2017.
 */
public class CustomBasicAuthenticationEntryPoint extends BasicAuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        //auth failed
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.addHeader("WWW-Authenticate", "Basic realm=" + getRealmName() + "");

        Log.get().logMessage(LogLevel.INFORMATION, "HTTP Status 401 : " + authException.getMessage());

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        setRealmName("NeChatRealm");
        super.afterPropertiesSet();
    }
}
