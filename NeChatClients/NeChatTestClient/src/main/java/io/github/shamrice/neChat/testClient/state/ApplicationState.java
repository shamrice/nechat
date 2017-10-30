package io.github.shamrice.neChat.testClient.state;

import io.github.shamrice.neChat.testClient.web.services.NeChatRestClient;

/**
 * Created by Erik on 10/30/2017.
 */
public class ApplicationState {

    private static NeChatRestClient neChatRestClient;

    private static String login = "";
    private static String password = "";
    private static String authToken = "";
    private static String result = "";

    public static void setNeChatRestClient(NeChatRestClient neChatRestClientToUse) {
        neChatRestClient = neChatRestClientToUse;
    }

    public static NeChatRestClient getNeChatRestClient() {
        return neChatRestClient;
    }

    public static void setLogin(String newLogin) {
        login = newLogin;
    }

    public static String getLogin() {
        return login;
    }

    public static void setPassword(String newPassword) {
        password = newPassword;
    }

    public static String getPassword() {
        return password;
    }

    public static void setAuthToken(String newAuthToken) {
        authToken = newAuthToken;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setResult(String newResult) {
        result = newResult;
    }

    public static String getResult() {
        return result;
    }
}
