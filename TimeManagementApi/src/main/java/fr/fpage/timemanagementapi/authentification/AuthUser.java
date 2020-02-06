package fr.fpage.timemanagementapi.authentification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(value = {"password", "userStatus"})
public class AuthUser {

    private UUID uuid;
    private String username;
    private String email;
    private List<String> userPermissions;

    public AuthUser() {}

    public AuthUser(UUID uuid, String username, String email, List<String> userPermissions) {
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

    public boolean hasPermission(String node)
    {
        return this.userPermissions.contains(node);
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", userPermissions=" + userPermissions +
                '}';
    }
}
