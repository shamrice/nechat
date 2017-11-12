package io.github.shamrice.neChat.testClient.web.services.requests;

import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.testClient.web.services.credentials.UserCredentials;
import org.json.JSONObject;

/**
 * Created by Erik on 11/12/2017.
 */
public class AuthorizationRequests extends RequestsBase {

    public AuthorizationRequests(UserCredentials userCredentials, ClientConfiguration clientConfiguration) {
        super(userCredentials, clientConfiguration);
    }

    public String getAuthToken() {

        JSONObject response = executeRequest(
                "GET",
                "auth/token/" + userCredentials.getLogin(),
                null);

        String authToken = response.get("authToken").toString();
        userCredentials.setAuthToken(authToken);

        return authToken;
    }


}
