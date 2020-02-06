package fr.fpage.conservatoireuserapi.authentification;

import java.util.UUID;

public class UserToken {

    private UUID uuid;

    public UserToken(UUID uuid) {
        this.uuid = uuid;
    }

    public UserToken(String uuid) {
        this.uuid = UUID.fromString(uuid);
    }

    public UUID getUuid() {
        return uuid;
    }
}
