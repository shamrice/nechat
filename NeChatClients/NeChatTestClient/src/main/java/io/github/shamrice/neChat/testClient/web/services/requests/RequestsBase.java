package io.github.shamrice.neChat.testClient.web.services.requests;

import io.github.shamrice.neChat.testClient.web.services.configuration.ClientConfiguration;
import io.github.shamrice.neChat.testClient.web.services.credentials.UserCredentials;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

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

    protected JSONObject executeRequest(String method, String resource, Map<String, String> requestHeaders) {

        JSONObject jsonObject = null;
        HttpURLConnection connection = null;

        try {

            String encoding = Base64.getEncoder().encodeToString(
                    (userCredentials.getLogin() + ":" + userCredentials.getPassword()).getBytes("UTF-8")
            );

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
                response.append('\r');
            }
            reader.close();

            System.out.println(resource + "-RESPONSE: " + response.toString());

            jsonObject = new JSONObject(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
/*
    protected List<JSONObject> executeRequestWithResultsList(String method, String resource, Map<String, String> requestHeaders) {

        List<JSONObject> results = new ArrayList<>();
        HttpURLConnection connection = null;

        try {

            String encoding = Base64.getEncoder().encodeToString(
                    (userCredentials.getLogin() + ":" + userCredentials.getPassword()).getBytes("UTF-8")
            );

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
          //  StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                JSONObject jsonObject = new JSONObject(line);
                results.add(jsonObject);
                //response.append(line);
                //response.append('\r');
            }
            reader.close();

           // System.out.println(resource + "-RESPONSE: " + response.toString());

          //  JSONObject jsonObject = new JSONObject(response.toString());

          //  results.add(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
    */

}
