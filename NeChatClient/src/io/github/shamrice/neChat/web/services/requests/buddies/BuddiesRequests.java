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

        if (buddyLogin != null) {
            JSONObject response = executeRequest(
                    "PUT",
                    "buddies/" + buddyLogin + "/",
                    getTokenRequestHeader()
            );

            if (response != null) {
                return new StatusResponse(
                        response.getString("status"),
                        response.getString("message")
                );
            } else {
                return new StatusResponse(
                        "failure",
                        "Unable to add buddy " + buddyLogin + ". Server did not respond to the request."
                );
            }
        }
        return new StatusResponse(
                "failure",
                "Buddy must be named in order to be added."
        );
    }

    public StatusResponse removeBuddy(String buddyLogin) {
        if (buddyLogin != null) {
            JSONObject response = executeRequest(
                    "DELETE",
                    "buddies/" + buddyLogin + "/",
                    getTokenRequestHeader()
            );

            if (response != null) {
                return new StatusResponse(
                        response.getString("status"),
                        response.getString("message")
                );
            } else {
                return new StatusResponse(
                        "failure",
                        "Unable to remove buddy. Server did not response to the request."
                );
            }
        }
        return new StatusResponse(
                "failure",
                "Must name a buddy to be removed."
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
