package fr.fpage.authentification.responses;

import java.util.List;
import java.util.UUID;

public class UserEntityResponse {

    private UUID uuid;
    private String username;
    private String email;
    private List<String> userPermissions;

    public UserEntityResponse(UUID uuid, String username, String email, List<String> userPermissions) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.userPermissions = userPermissions;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getUserPermissions() {
        return userPermissions;
    }
}
