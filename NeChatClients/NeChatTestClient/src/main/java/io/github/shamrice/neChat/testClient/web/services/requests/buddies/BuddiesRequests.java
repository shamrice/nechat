package io.github.shamrice.neChat.testClient.web.services.requests.buddies;

import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.testClient.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.testClient.web.services.requests.RequestsBase;
import io.github.shamrice.neChat.testClient.web.services.requests.StatusResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Erik on 11/12/2017.
 */
public class BuddiesRequests extends RequestsBase {

    public BuddiesRequests(UserCredentials userCredentials, ClientConfiguration clientConfiguration) {
        super(userCredentials, clientConfiguration);
    }

    public StatusResponse addBuddy(String buddyLogin) {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("token", userCredentials.getAuthToken());

        JSONObject response = executeRequest(
                "PUT",
                "buddies/" + buddyLogin + "/",
                requestHeaders
        );

        boolean success = false;
        if (response.getString("status").equals("SUCCESS")) {
            success = true;
        }

        return new StatusResponse(
                success,
                response.getString("status"),
                response.getString("message")
        );
    }

    public BuddiesResponse getBuddies() {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("token", userCredentials.getAuthToken());

        JSONObject response = executeRequest(
                "GET",
                "buddies",
                requestHeaders
        );

        List<Buddy> buddyList = new ArrayList<>();
        JSONArray buddyListJSONArray = response.getJSONArray("buddies");

        for (Object buddyObj : buddyListJSONArray) {
            JSONObject buddyJSON = (JSONObject) buddyObj;
            Buddy buddyToAdd = new Buddy(
                    buddyJSON.getInt("userId"),
                    buddyJSON.getString("login")
            );
        }

        return new BuddiesResponse(
                response.getString("login"),
                buddyList
        );
    }
}
