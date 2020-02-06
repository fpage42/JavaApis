package fr.fpage.authentification.model;

import fr.fpage.authentification.Application;
import fr.fpage.authentification.exception.TokenNotExistException;

import java.sql.*;
import java.util.UUID;

public class Resource {

    private UUID uuid;
    private UUID userUuid;
    private String name;

    public Resource(UUID uuid) {
        this.uuid = uuid;
        this.loadRessource();
    }


    public Resource(UUID userUuid, String name) {
        this.uuid = UUID.randomUUID();
        this.userUuid = userUuid;
        this.name = name;
        this.createRessource();
    }

    private void createRessource() {
        try {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `ressource` (`uuid`, `userUuid`, `name`) VALUES (?, ?, ?)");
            preparedStatement.setString(1, this.uuid.toString());
            preparedStatement.setString(2, this.userUuid.toString());
            preparedStatement.setString(3, this.name);
            preparedStatement.execute();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadRessource() {
        try {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `ressource` WHERE `uuid`=?");
            preparedStatement.setString(1, this.uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.userUuid = UUID.fromString(res.getString("userUuid"));
                this.name = res.getString("name");
            }
            connection.close();
        } catch (SQLException ignored) {
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public String getName() {
        return name;
    }
}
