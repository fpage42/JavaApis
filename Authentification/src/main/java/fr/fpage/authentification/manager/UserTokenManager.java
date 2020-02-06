package fr.fpage.authentification.manager;

import fr.fpage.authentification.exception.TokenNotExistException;
import fr.fpage.authentification.model.RefreshToken;
import fr.fpage.authentification.model.UserToken;

import java.util.HashMap;
import java.util.UUID;

public class UserTokenManager {
    private static UserTokenManager ourInstance = new UserTokenManager();

    public static UserTokenManager getInstance() {
        return ourInstance;
    }

    private HashMap<UUID, UserToken> userTokens = new HashMap<>();

    private UserTokenManager() {
    }

    public UserToken getUserToken(String userToken) throws TokenNotExistException {
        return this.getUserToken(UUID.fromString(userToken));
    }
    public UserToken getUserToken(UUID userToken) throws TokenNotExistException {
        if (!this.userTokens.containsKey(userToken))
            this.userTokens.put(userToken, new UserToken(userToken));
        else
            if (!this.userTokens.get(userToken).isValide())
                this.userTokens.remove(userToken);
        return this.userTokens.get(userToken);
    }

    public UserToken createUserToken(UUID clientToken, UUID UserUuid)
    {
        UserToken userToken = new UserToken(clientToken, UserUuid);
        this.userTokens.put(userToken.getToken(), userToken);
        return userToken;
    }

}
