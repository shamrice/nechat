package io.github.shamrice.neChat.web.services.requests.authorization;

import io.github.shamrice.neChat.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.web.services.requests.RequestsBase;
import org.json.JSONObject;

import java.sql.Date;

/**
 * Created by Erik on 11/12/2017.
 */
public class AuthorizationRequests extends RequestsBase {

    public AuthorizationRequests(UserCredentials userCredentials, ClientConfiguration clientConfiguration) {
        super(userCredentials, clientConfiguration);
    }

    public AuthorizationResponse getAuthToken() {

        AuthorizationResponse result = null;

        JSONObject response = executeRequest(
                "GET",
                "auth/token/" + userCredentials.getLogin(),
                null);

        String authToken = response.get("authToken").toString();
        userCredentials.setAuthToken(authToken);

        result = new AuthorizationResponse(
                response.getInt("tokenAuthId"),
                response.getInt("userId"),
                response.getString("authToken"),
                Date.valueOf(response.getString("create")),
                Date.valueOf(response.getString("expire"))
        );

        return result;
    }
}
