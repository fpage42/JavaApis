package fr.fpage.authentification.manager;

import fr.fpage.authentification.exception.TokenNotExistException;
import fr.fpage.authentification.model.RefreshToken;
import fr.fpage.authentification.requests.RefreshTokenRequest;

import java.util.HashMap;
import java.util.UUID;

public class RefreshTokenManager {
    private static RefreshTokenManager ourInstance = new RefreshTokenManager();

    public static RefreshTokenManager getInstance() {
        return ourInstance;
    }

    private HashMap<UUID, RefreshToken> refreshTokens = new HashMap<>();

    private RefreshTokenManager() {
    }

    public RefreshToken getRefreshToken(UUID refreshToken) throws TokenNotExistException {
        if (!this.refreshTokens.containsKey(refreshToken))
            this.refreshTokens.put(refreshToken, new RefreshToken(refreshToken));
        else
            if (!this.refreshTokens.get(refreshToken).isValide()) {
                this.refreshTokens.remove(refreshToken);
                throw new TokenNotExistException(refreshToken, TokenNotExistException.TokenNotExistExceptionReason.INVALID);
            }
        return this.refreshTokens.get(refreshToken);
    }

    public RefreshToken createRefreshToken(RefreshTokenRequest refreshTokenRequest) throws TokenNotExistException {
        RefreshToken refreshToken = new RefreshToken(refreshTokenRequest.getUserToken().getUserUuid(), refreshTokenRequest.getClientId());
        this.refreshTokens.put(refreshToken.getToken(), refreshToken);
        return refreshToken;
    }
}
