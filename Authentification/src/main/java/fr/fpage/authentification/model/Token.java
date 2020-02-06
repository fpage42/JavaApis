package fr.fpage.authentification.model;

import fr.fpage.authentification.Application;
import fr.fpage.authentification.exception.TokenNotExistException;

import java.sql.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/*
 * CREATE TABLE `auth`.`(refreshToken|userToken)`
 * ( `uuid` VARCHAR(40) NOT NULL ,
 *   `expire` DATETIME NOT NULL ,
 *   `userUuid` VARCHAR(40) NOT NULL ,
 *   `clientUuid` VARCHAR(40) NOT NULL,
  *   UNIQUE (`uuid`));
 */

public abstract class Token {

    private UUID token;
    private UUID userUuid;
    private UUID clientUuid;
    private Instant expire;

    public Token(UUID token) throws TokenNotExistException {
        this.token = token;
        this.loadToken();
    }

    public Token(UUID clientUuid, UUID userUuid) {
        this.userUuid = userUuid;
        this.clientUuid = clientUuid;
        this.token = UUID.randomUUID();
        this.expire = this.getExpireInstant();
        this.saveToken();
        try {
            this.loadToken();
        } catch (TokenNotExistException e) {
            e.printStackTrace();
        }
    }

    private void saveToken() {
        try {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `" + this.getDatabase() + "` (`uuid`, `expire`, `userUuid`, `clientUuid`) VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, this.token.toString());
            preparedStatement.setTimestamp(2, Timestamp.from(this.expire));
            preparedStatement.setString(3, this.userUuid.toString());
            preparedStatement.setString(4, this.clientUuid.toString());
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadToken() throws TokenNotExistException {
        try {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `" + this.getDatabase() + "` WHERE `uuid`=? AND `expire`>NOW()");
            preparedStatement.setString(1, this.token.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.userUuid = UUID.fromString(res.getString("userUuid"));
                this.clientUuid = UUID.fromString(res.getString("clientUuid"));
                this.expire = res.getTimestamp("expire").toInstant();
            }
            connection.close();
        } catch (SQLException e) {
            throw new TokenNotExistException(this.token, TokenNotExistException.TokenNotExistExceptionReason.NOT_FOUND);
        }
        if (this.userUuid == null) {
            this.deleteToken();
            throw new TokenNotExistException(this.token, TokenNotExistException.TokenNotExistExceptionReason.INVALID);
        }
    }

    public boolean isValide() {
        return this.expire.isAfter(Instant.now());
    }

    private void deleteToken() {
        try {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `refreshtoken` WHERE `uuid`=?");
            preparedStatement.setString(1, this.token.toString());
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UUID getToken() {
        return token;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public UUID getClientUuid() {
        return clientUuid;
    }

    protected abstract String getDatabase();
    protected abstract Instant getExpireInstant();

}
