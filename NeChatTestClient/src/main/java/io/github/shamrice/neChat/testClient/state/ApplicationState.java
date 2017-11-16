package io.github.shamrice.neChat.testClient.state;

import io.github.shamrice.neChat.testClient.web.services.NeChatRestClient;
import io.github.shamrice.neChat.testClient.web.services.credentials.UserCredentials;

/**
 * Created by Erik on 10/30/2017.
 */
public class ApplicationState {

    private static NeChatRestClient neChatRestClient;
    private static String result = "";

    public static void setNeChatRestClient(NeChatRestClient neChatRestClientToUse) {
        neChatRestClient = neChatRestClientToUse;
    }

    public static NeChatRestClient getNeChatRestClient() {
        return neChatRestClient;
    }

    public static void setResult(String newResult) {
        result = newResult;
    }

    public static String getResult() {
        return result;
    }
}
