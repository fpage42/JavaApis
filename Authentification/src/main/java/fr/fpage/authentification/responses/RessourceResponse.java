package fr.fpage.authentification.responses;

import java.util.UUID;

public class RessourceResponse {

    private UUID uuid;

    public RessourceResponse(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }
}
