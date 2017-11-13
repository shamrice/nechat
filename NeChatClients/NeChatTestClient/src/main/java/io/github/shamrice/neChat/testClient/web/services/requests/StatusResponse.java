package io.github.shamrice.neChat.testClient.web.services.requests;

/**
 * Created by Erik on 11/13/2017.
 */
public class StatusResponse {
    private String status;
    private String message;
    private boolean success;

    public StatusResponse(boolean success, String status, String message) {
        this.success = success;
        this.status = status;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
