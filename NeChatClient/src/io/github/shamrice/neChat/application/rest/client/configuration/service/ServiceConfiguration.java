package io.github.shamrice.neChat.application.rest.client.configuration.service;

/**
 * Created by Erik on 11/12/2017.
 */
public class ServiceConfiguration {

    private String serviceProtocol;
    private String serviceHost;
    private int servicePort;
    private String serviceWebApp;

    public ServiceConfiguration(String serviceHost, int servicePort, String serviceProtocol, String serviceWebApp) {
        this.serviceHost = serviceHost;
        this.servicePort = servicePort;
        this.serviceProtocol = serviceProtocol;
        this.serviceWebApp = serviceWebApp;
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

    public String getServiceWebApp() {
        return serviceWebApp;
    }

    public String getServiceUrl() {
        return serviceProtocol + "://" + serviceHost + ":" + servicePort + "/" + serviceWebApp + "/";
    }

}
