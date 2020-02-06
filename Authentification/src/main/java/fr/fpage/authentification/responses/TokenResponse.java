package fr.fpage.authentification.responses;

public class TokenResponse {

    private String uuid;
    private String message;

    public TokenResponse(String token, String message) {
        uuid = token;
        this.message = message;
    }

    public String getUuid() {
        return uuid;
    }

    public String getMessage() {
        return message;
    }
}
