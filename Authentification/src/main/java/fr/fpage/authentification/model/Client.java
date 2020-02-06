package fr.fpage.authentification.model;

/*
 * CREATE TABLE `auth`.`clients`
 * ( `uuid` VARCHAR(40) NOT NULL ,
 *   `redirectUrl` VARCHAR(100) NOT NULL ,
 *   `permissionLevel` INTEGER ,
 *   `userUuid` VARCHAR(40) NOT NULL );
 */

import fr.fpage.authentification.Application;
import fr.fpage.authentification.requests.ClientCreateRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Client {

    private UUID uuid;
    private String redirectUrl;
    private int permissionLevel;
    private UUID userUuid;

    public Client(UUID uuid) {
        this.uuid = uuid;
        this.loadClient();
    }

    public Client(ClientCreateRequest client) {
        this.redirectUrl = client.getRedirectUrl();
        this.userUuid = client.getUserUuid();
        this.createClient();
    }

    private void createClient() {
        try {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `clients` (`uuid`, `redirectLink`, `userUuid`) VALUES (?, ?, ?)");
            this.uuid = UUID.randomUUID();
            preparedStatement.setString(1, this.uuid.toString());
            preparedStatement.setString(2, this.redirectUrl);
            preparedStatement.setString(3, this.userUuid.toString());
            preparedStatement.execute();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadClient() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `clients` WHERE `uuid`=?");
            preparedStatement.setString(1, this.uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.redirectUrl = res.getString("redirectLink");
                this.userUuid = UUID.fromString(res.getString("userUuid"));
                this.permissionLevel = res.getInt("permissionLevel");
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public UUID getUserUuid() {
        return userUuid;
    }
}
