package fr.fpage.authentification.requests;

import fr.fpage.authentification.exception.TokenNotExistException;
import fr.fpage.authentification.manager.UserTokenManager;
import fr.fpage.authentification.model.UserToken;

import java.util.UUID;

public class UserTokenResolveRequest {

    private String token;

    public UserTokenResolveRequest(String userToken) {
        this.token = userToken;
    }

    public String getToken() {
        return token;
    }

    public UserToken getUserToken()
    {
        try {
            return UserTokenManager.getInstance().getUserToken(UUID.fromString(this.token));
        } catch (TokenNotExistException e) {
            return null;
        }
    }
}
