package io.github.shamrice.neChat.testClient.web.services.configuration;

import io.github.shamrice.neChat.testClient.web.services.configuration.service.ServiceConfiguration;

/**
 * Created by Erik on 10/30/2017.
 */
public class ClientConfiguration {

    private ServiceConfiguration serviceConfiguration;

    public ClientConfiguration(ServiceConfiguration serviceConfiguration) {
        this.serviceConfiguration = serviceConfiguration;
    }

    public String getServiceUrl() {
        return serviceConfiguration.getServiceUrl();
    }
}
