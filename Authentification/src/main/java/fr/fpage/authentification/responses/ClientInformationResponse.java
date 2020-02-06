package fr.fpage.authentification.responses;

public class ClientInformationResponse {

    private String redirectUri;

    public ClientInformationResponse(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }
}
