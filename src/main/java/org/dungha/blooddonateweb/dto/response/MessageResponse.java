package org.dungha.blooddonateweb.dto.response;

import org.dungha.blooddonateweb.model.Donation;

public class MessageResponse {

    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public MessageResponse(String message, String token) {
    }

    public MessageResponse(String donationCreatedSuccessfully, Donation savedDonation) {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
