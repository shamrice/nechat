package io.github.shamrice.neChat.testClient.web.services.configuration.service;

/**
 * Created by Erik on 11/12/2017.
 */
public class ServiceConfiguration {

    private String serviceProtocol;
    private String serviceHost;
    private int servicePort;

    public ServiceConfiguration(String serviceHost, int servicePort, String serviceProtocol) {
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.serviceProtocol = serviceProtocol;
    }

    public String getServiceHost() {
        return serviceHost;
    }

    public String getServiceProtocol() {
        return serviceProtocol;
    }

    public int getServicePort() {
        return servicePort;
    }

    public String getServiceUrl() {
        return serviceProtocol + "://" + serviceHost + ":" + servicePort + "/";
    }
}
