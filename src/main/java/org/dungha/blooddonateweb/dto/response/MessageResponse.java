package org.dungha.blooddonateweb.dto.response;

public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, String token) {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
