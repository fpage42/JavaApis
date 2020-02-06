package fr.fpage.authentification.exception;

import java.util.UUID;

public class TokenNotExistException extends Exception {

    private UUID token;
    private TokenNotExistExceptionReason reason;

    public TokenNotExistException(UUID token, TokenNotExistExceptionReason reason)
    {
        this.token = token;
        this.reason = reason;
    }

    public UUID getToken() {
        return token;
    }

    public TokenNotExistExceptionReason getReason() {
        return reason;
    }

    public enum TokenNotExistExceptionReason
    {
        NOT_FOUND,
        INVALID;
    }

}
