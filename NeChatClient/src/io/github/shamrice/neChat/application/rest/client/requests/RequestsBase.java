package io.github.shamrice.neChat.application.rest.client.requests;

import io.github.shamrice.neChat.application.rest.client.configuration.ClientConfiguration;
import io.github.shamrice.neChat.application.rest.client.credentials.UserCredentials;
import io.github.shamrice.nechat.logging.Log;
import io.github.shamrice.nechat.logging.LogLevel;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by Erik on 11/12/2017.
 */
public abstract class RequestsBase {

    protected UserCredentials userCredentials;
    protected ClientConfiguration clientConfiguration;

    public RequestsBase(UserCredentials userCredentials, ClientConfiguration clientConfiguration) {
        this.userCredentials = userCredentials;
        this.clientConfiguration = clientConfiguration;
    }

    protected Map<String, String> getTokenRequestHeader() {
        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("token", userCredentials.getAuthToken());
        return requestHeaders;
    }

    protected JSONObject executeRequest(String method, String resource, Map<String, String> requestHeaders) {
        return executeRequest(method, resource, requestHeaders, "");
    }

    protected JSONObject executeRequest(String method, String resource, Map<String, String> requestHeaders, String body) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        //debug
        Log.get().logMessage(LogLevel.DEBUG, "METHOD: " + method + " RESOURCE: " + resource);

        if (requestHeaders != null) {
            for (String key : requestHeaders.keySet()) {
                Log.get().logMessage(LogLevel.DEBUG, key + " : " + requestHeaders.get(key));
            }
        }

        try {

            String encoding = Base64.getEncoder().encodeToString(
                    (userCredentials.getLogin() + ":" + userCredentials.getPassword()).getBytes("UTF-8")
            );

            URL url = new URL(clientConfiguration.getServiceUrl() + resource);
            connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Authorization", "Basic " + encoding);

            if (requestHeaders != null) {
                for (String key : requestHeaders.keySet()) {
                    connection.setRequestProperty(key, requestHeaders.get(key));
                }
            }

            if (!body.equals("")) {
                connection.setRequestProperty("Content-Type", "application/text; charset-UTF-8"); //TODO : SET TO JSON

                OutputStream os = connection.getOutputStream();
                os.write(body.getBytes("UTF-8"));
                os.flush();
                os.close();
            }
            StringBuilder response = new StringBuilder();

            InputStream content = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            reader.close();
            jsonObject = new JSONObject(response.toString());

            Log.get().logMessage(LogLevel.DEBUG, resource + "-RESPONSE: " + response.toString());

        } catch (Exception e) {
            Log.get().logException(e);
        }

        return jsonObject;
    }
}
