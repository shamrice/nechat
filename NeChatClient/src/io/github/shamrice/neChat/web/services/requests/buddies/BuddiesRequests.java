package io.github.shamrice.neChat.web.services.requests.buddies;

import io.github.shamrice.neChat.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.web.services.requests.RequestsBase;
import io.github.shamrice.neChat.web.services.requests.StatusResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erik on 11/12/2017.
 */
public class BuddiesRequests extends RequestsBase {

    public BuddiesRequests(UserCredentials userCredentials, ClientConfiguration clientConfiguration) {
        super(userCredentials, clientConfiguration);
    }

    public StatusResponse addBuddy(String buddyLogin) {

        JSONObject response = executeRequest(
                "PUT",
                "buddies/" + buddyLogin + "/",
                getTokenRequestHeader()
        );

        return new StatusResponse(
                response.getString("status"),
                response.getString("message")
        );
    }

    public StatusResponse removeBuddy(String buddyLogin) {
        JSONObject response = executeRequest(
                "DELETE",
                "buddies/" + buddyLogin + "/",
                getTokenRequestHeader()
        );

        return new StatusResponse(
                response.getString("status"),
                response.getString("message")
        );
    }

    public BuddiesResponse getBuddies() {

        JSONObject response = executeRequest(
                "GET",
                "buddies",
                getTokenRequestHeader()
        );

        List<Buddy> buddyList = new ArrayList<>();
        JSONArray buddyListJSONArray = response.getJSONArray("buddies");

        for (Object buddyObj : buddyListJSONArray) {
            JSONObject buddyJSON = (JSONObject) buddyObj;
            Buddy buddyToAdd = new Buddy(
                    buddyJSON.getInt("userId"),
                    buddyJSON.getString("login")
            );
            buddyList.add(buddyToAdd);
        }

        return new BuddiesResponse(
                response.getString("login"),
                buddyList
        );
    }
}
