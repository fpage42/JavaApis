package fr.fpage.conservatoireuserapi.models;

import fr.fpage.conservatoireuserapi.Application;
import fr.fpage.conservatoireuserapi.authentification.AuthUser;
import fr.fpage.conservatoireuserapi.authentification.UserUUID;
import fr.fpage.conservatoireuserapi.exceptions.UserLoadException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class ReduceUser {

    private String firstName, lastName, email;
    private UUID uuid;

    public ReduceUser(AuthUser user) {
        this.uuid = user.getUuid();
        this.email = user.getEmail();
        try {
            this.load();
        } catch (UserLoadException ignored) {
        }
    }

    @SuppressWarnings("Duplicates")
    private void load() throws UserLoadException {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `firstName`, `lastName` FROM `conservatoireUsers`.`users` WHERE `uuid`=?");
            preparedStatement.setString(1, this.uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.firstName = res.getString("firstName");
                this.lastName = res.getString("lastName");
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
            throw new UserLoadException(UserLoadException.UserExceptionReasons.UUID_NOT_FOUND);
        }

    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public UUID getUuid() {
        return uuid;
    }
}
