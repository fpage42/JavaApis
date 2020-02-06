package fr.fpage.authentification.requests;

import java.util.UUID;

public class ClientCreateRequest {

    private String redirectUrl;
    private UUID userUuid;

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public UUID getUserUuid() {
        return userUuid;
    }
}
