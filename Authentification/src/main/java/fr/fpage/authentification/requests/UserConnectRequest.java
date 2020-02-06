package fr.fpage.authentification.requests;

import fr.fpage.authentification.Application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.regex.Pattern;

public class UserConnectRequest {

    private String username;
    private String password;
    private UUID uuid;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UUID connect() {
        if (username != null) {
            if (this.checkEmailRules(username)) {
                this.loadUserEmailPassword();
            }
            else
            {
                this.loadUserNamePassword();
            }
        }
        return this.uuid;
    }

    private boolean checkEmailRules(String email)
    {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        if (!pattern.matcher(email).matches())
        {
            return false;
        }
        return true;
    }

    private void loadUserNamePassword() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `uuid` FROM `users` WHERE `username`=? AND `password`=?");
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.password);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.uuid = UUID.fromString(res.getString("uuid"));
            }
            connection.close();
        }
        catch (SQLException e)
        {}
    }

    private void loadUserEmailPassword() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `uuid` FROM `users` WHERE `email`=? AND `password`=?");
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.password);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.uuid = UUID.fromString(res.getString("uuid"));
            }
            connection.close();
        }
        catch (SQLException e)
        {}
    }
}
