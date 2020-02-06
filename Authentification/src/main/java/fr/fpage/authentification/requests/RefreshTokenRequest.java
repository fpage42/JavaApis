package fr.fpage.authentification.requests;

import fr.fpage.authentification.exception.TokenNotExistException;
import fr.fpage.authentification.manager.UserTokenManager;
import fr.fpage.authentification.model.UserToken;

import java.util.UUID;

public class RefreshTokenRequest {

    private UUID clientId;
    private UUID userTokenId;

    public RefreshTokenRequest(UUID clientId, UUID userTokenId) {
        this.clientId = clientId;
        this.userTokenId = userTokenId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public UUID getUserTokenId() {
        return userTokenId;
    }

    public UserToken getUserToken() throws TokenNotExistException {
        return UserTokenManager.getInstance().getUserToken(this.userTokenId);
    }
}
