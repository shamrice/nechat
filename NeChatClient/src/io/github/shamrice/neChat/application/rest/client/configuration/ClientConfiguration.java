package io.github.shamrice.neChat.application.rest.client.configuration;

import io.github.shamrice.neChat.application.rest.client.configuration.service.ServiceConfiguration;
import io.github.shamrice.neChat.application.rest.client.credentials.UserCredentials;

/**
 * Created by Erik on 10/30/2017.
 */
public class ClientConfiguration {

    private ServiceConfiguration serviceConfiguration;
    private UserCredentials userCredentials;

    public ClientConfiguration(ServiceConfiguration serviceConfiguration, UserCredentials userCredentials) {
        this.serviceConfiguration = serviceConfiguration;
        this.userCredentials = userCredentials;
    }

    public String getServiceUrl() {
        return serviceConfiguration.getServiceUrl();
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

}
