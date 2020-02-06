package fr.fpage.timemanagementapi.models;

import fr.fpage.timemanagementapi.Application;
import fr.fpage.timemanagementapi.exceptions.BddLoadException;
import fr.fpage.timemanagementapi.requests.CreateSalleRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Salle {

    private UUID uuid;
    private String name;
    private Integer size;

    public Salle(CreateSalleRequest createSalleRequest) {
        this.uuid = UUID.randomUUID();
        this.name = createSalleRequest.getName();
        this.size = createSalleRequest.getSize();
        this.createSalle();
    }

    public Salle(UUID uuid) throws BddLoadException {
        this.uuid = uuid;
        this.loadSalle();
    }

    private void loadSalle() throws BddLoadException {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `timeManagement`.`salles` WHERE `uuid`=?");
            preparedStatement.setString(1, this.uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.name = res.getString("name");
                this.size = res.getInt("size");
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
            throw new BddLoadException(BddLoadException.BddLoadExceptionReasons.UUID_NOT_FOUND);
        }
    }

    private void createSalle() {
        try {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `timeManagement`.`salles` (`uuid`, `name`, `size`) VALUES (?, ?, ?)");
            preparedStatement.setString(1, this.uuid.toString());
            preparedStatement.setString(2, this.name);
            preparedStatement.setInt(3, this.size);
            preparedStatement.execute();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Integer getSize() {
        return size;
    }
}
