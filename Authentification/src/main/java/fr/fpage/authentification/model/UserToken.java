package fr.fpage.authentification.model;

import fr.fpage.authentification.exception.TokenNotExistException;
import fr.fpage.authentification.manager.UserManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class UserToken extends Token {

    public UserToken(UUID token) throws TokenNotExistException {
        super(token);
    }

    public UserToken(UUID authorization, UUID userUuid) {
        super(authorization, userUuid);
    }

    @Override
    protected String getDatabase() {
        return "usertoken";
    }

    @Override
    protected Instant getExpireInstant() {
        return Instant.now().plus(60, ChronoUnit.SECONDS);
    }

    public User getUser()
    {
        return UserManager.getInstance().getUser(this.getUserUuid());
    }
}
