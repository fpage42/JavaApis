package fr.fpage.conservatoireuserapi.authentification;

import java.util.UUID;

public class UserUUID {

    private UUID uuid;

    public UserUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return this.uuid.toString();
    }
}
