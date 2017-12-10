package io.github.shamrice.neChat.application.client.context;

import io.github.shamrice.neChat.application.rest.client.NeChatRestClient;
import io.github.shamrice.neChat.application.rest.client.NeChatRestClientImpl;
import io.github.shamrice.neChat.application.rest.client.configuration.ConfigurationBuilder;

/**
 * Created by Erik on 11/15/2017.
 */
public class ApplicationContext {

    private static ApplicationContext applicationContext;
    private NeChatRestClient neChatRestClient;
    private String selectedBuddyLogin = "";

    public static ApplicationContext get() {
        if (applicationContext == null) {
            applicationContext = new ApplicationContext();
        }

        return applicationContext;
    }

    private ApplicationContext() {
        neChatRestClient = new NeChatRestClientImpl(ConfigurationBuilder.build());
    }

    public String getCurrentLogin() {
        return neChatRestClient.getCurrentLogin();
    }

    public NeChatRestClient getNeChatRestClient() {
        return neChatRestClient;
    }

    public void setSelectedBuddyLogin(String selectedBuddyLogin) {
        this.selectedBuddyLogin = selectedBuddyLogin;
    }

    public String getSelectedBuddyLogin() {
        return selectedBuddyLogin;
    }
}
