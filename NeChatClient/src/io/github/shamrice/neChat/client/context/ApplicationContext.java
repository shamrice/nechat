package io.github.shamrice.neChat.client.context;

import io.github.shamrice.neChat.web.services.NeChatRestClient;
import io.github.shamrice.neChat.web.services.configuration.ConfigurationBuilder;
import io.github.shamrice.neChat.web.services.credentials.UserCredentials;
import io.github.shamrice.neChat.web.services.requests.buddies.Buddy;

/**
 * Created by Erik on 11/15/2017.
 */
public class ApplicationContext {

    private static ApplicationContext applicationContext;
    private NeChatRestClient neChatRestClient;
    private UserCredentials userCredentials;
    private String selectedBuddyLogin = "";

    public static ApplicationContext get() {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext();
        }

        return applicationContext;
    }

    private ApplicationContext() {
        neChatRestClient = new NeChatRestClient(ConfigurationBuilder.build());
    }

    public NeChatRestClient getNeChatRestClient() {
        return neChatRestClient;
    }

    public void setUserCredentials(UserCredentials userCredentials) {
        this.userCredentials = userCredentials;
        this.neChatRestClient.setUserCredentials(userCredentials);
    }

    public void setSelectedBuddyLogin(String selectedBuddyLogin) {
        this.selectedBuddyLogin = selectedBuddyLogin;
    }

    public String getSelectedBuddyLogin() {
        return selectedBuddyLogin;
    }
}