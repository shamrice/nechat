package io.github.shamrice.neChat.application.rest.client.requests;

/**
 * Created by Erik on 11/13/2017.
 */
public class StatusResponse implements Response {
    private String status;
    private String message;

    public StatusResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public boolean isSuccess() {
        return status.toLowerCase().equals("success");
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
