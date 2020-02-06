package fr.fpage.authentification.responses;

import java.util.UUID;

public class UserResponse {

    private String message;
    private UUID uuid;

    public UserResponse(String message, UUID uuid) {
        this.message = message;
        this.uuid = uuid;
    }

    public String getMessage() {
        return message;
    }

    public UUID getUuid() {
        return uuid;
    }
}
