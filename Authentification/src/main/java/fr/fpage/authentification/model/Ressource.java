package fr.fpage.authentification.model;

import fr.fpage.authentification.Application;
import fr.fpage.authentification.requests.RessourceCreateRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/*
* CREATE TABLE `auth`.`ressources`
* ( `UUID` VARCHAR(40) NOT NULL ,
* `userUuid` VARCHAR(40) NOT NULL ,
* `name` VARCHAR(200) NOT NULL ,
 *  UNIQUE (`UUID`),
 *  UNIQUE (`name`))
 */
public class Ressource {

    private UUID uuid;
    private UUID userUuid;
    private String name;

    public Ressource(UUID uuid)
    {
        this.uuid = uuid;
        this.loadRessource();
    }

    public Ressource(RessourceCreateRequest ressourceCreateRequest, UUID userUuid) {
        this.userUuid = userUuid;
        this.name = ressourceCreateRequest.getName();
        this.uuid = UUID.randomUUID();
        this.save();
    }

    private void save() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `ressources` (`uuid`, `userUuid`, `name`) VALUES (?, ?, ?)");
            preparedStatement.setString(1, this.uuid.toString());
            preparedStatement.setString(2, this.userUuid.toString());
            preparedStatement.setString(3, this.name);
            preparedStatement.execute();
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }

    }

    private void loadRessource() {
        try
        {
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
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
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
