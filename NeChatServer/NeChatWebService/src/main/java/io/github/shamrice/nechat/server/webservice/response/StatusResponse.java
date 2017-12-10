package io.github.shamrice.nechat.server.webservice.response;

/**
 * Created by Erik on 11/13/2017.
 */
public class StatusResponse {

    private Status status;
    private String message;

    public StatusResponse(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
