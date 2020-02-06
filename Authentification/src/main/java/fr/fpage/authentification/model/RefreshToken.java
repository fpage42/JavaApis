package fr.fpage.authentification.model;

import fr.fpage.authentification.exception.TokenNotExistException;

import java.time.Instant;
import java.time.Period;
import java.util.UUID;

public class RefreshToken extends Token {

    public RefreshToken(UUID refreshToken) throws TokenNotExistException {
        super(refreshToken);
    }

    public RefreshToken(UUID userId, UUID clientId)
    {
        super(clientId, userId);
    }

    @Override
    protected String getDatabase() {
        return "refreshtoken";
    }

    @Override
    protected Instant getExpireInstant() {
        return Instant.now().plus(Period.ofDays(365));
    }
}
