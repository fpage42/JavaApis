package fr.fpage.conservatoireuserapi.models;

import fr.fpage.conservatoireuserapi.Application;
import fr.fpage.conservatoireuserapi.authentification.AuthUser;
import fr.fpage.conservatoireuserapi.exceptions.UserLoadException;
import fr.fpage.conservatoireuserapi.requests.ChangeUserDatasRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class User {

    private UUID uuid;
    private String username, email, firstName, lastName;
    private List<String> userPermissions;

    public User() {}

    public User(UUID uuid) {
        this.uuid = uuid;
        this.createUser();
    }

    public User(AuthUser authUser) throws UserLoadException {
        this.uuid = authUser.getUuid();
        this.username = authUser.getUsername();
        this.email = authUser.getEmail();
        this.userPermissions = authUser.getUserPermissions();
        this.loadUser();
    }

    private void createUser() {
        try {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `conservatoireUsers`.`users` (`uuid`, `firstName`, `lastName`) VALUES (?, ?, ?)");
            preparedStatement.setString(1, this.uuid.toString());
            preparedStatement.setString(2, this.firstName);
            preparedStatement.setString(3, this.lastName);
            preparedStatement.execute();
            connection.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("Duplicates")
    private void loadUser() throws UserLoadException {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `conservatoireUsers`.`users` WHERE `uuid`=?");
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

    public UUID getUuid() {
        return uuid;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getUserPermissions() {
        return userPermissions;
    }

    @Override
    public String toString() {
        return "AuthUser{" +
                "uuid=" + uuid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userPermissions=" + userPermissions +
                '}';
    }

    public void changeDatas(ChangeUserDatasRequest request) {
        if (request.getFirstName() != null)
            this.firstName = request.getFirstName();
        if (request.getLastName() != null)
            this.lastName = request.getLastName();
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE `conservatoireUsers`.`users` SET `firstName`=?, `lastName`=? WHERE `uuid`=?");
            preparedStatement.setString(1, this.firstName);
            preparedStatement.setString(2, this.lastName);
            preparedStatement.setString(3, this.uuid.toString());
            preparedStatement.execute();
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
    }

    public boolean hasPermission(String node)
    {
        return this.userPermissions.contains(node);
    }
}
