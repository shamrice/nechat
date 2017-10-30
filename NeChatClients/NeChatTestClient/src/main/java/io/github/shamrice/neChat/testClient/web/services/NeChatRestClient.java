package io.github.shamrice.neChat.testClient.web.services;

import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;
import jdk.nashorn.internal.parser.JSONParser;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Erik on 10/30/2017.
 */
public class NeChatRestClient {

    private ClientConfiguration clientConfiguration;

    public NeChatRestClient(ClientConfiguration clientConfiguration) {
        this.clientConfiguration = clientConfiguration;
    }

    public String getAuthToken(String login, String password) {

        JSONObject response = executeRequest("GET", "auth/token/" + login, login, password, null);
        return response.get("authToken").toString();
    }

    public String getBuddies(String login, String password, String authToken) {

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("token", authToken);

        JSONObject response = executeRequest("GET", "buddies", login, password, requestHeaders);
        return response.toString();
    }

    private JSONObject executeRequest(String method, String resource, String username,
                                      String password, Map<String, String> requestHeaders) {

        JSONObject jsonObject = null;

        HttpURLConnection connection = null;

        try {

            String encoding = Base64.getEncoder().encodeToString((username + ":" + password).getBytes("UTF-8"));

            URL url = new URL(clientConfiguration.getServiceUrl() + resource);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(method);
            connection.setRequestProperty("Authorization", "Basic " + encoding);

            if (requestHeaders != null) {
                for (String key : requestHeaders.keySet()) {
                    connection.setRequestProperty(key, requestHeaders.get(key));
                }
            }
            connection.setUseCaches(false);
            connection.setDoOutput(true);

            InputStream content = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.equals('\r');
            }
            reader.close();

            System.out.println(resource + "-RESPONSE: " + response.toString());

            jsonObject = new JSONObject(response.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
