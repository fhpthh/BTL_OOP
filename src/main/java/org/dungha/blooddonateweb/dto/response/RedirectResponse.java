package org.dungha.blooddonateweb.dto.response;

public class RedirectResponse {
    private String redirectUrl;

    public RedirectResponse(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
