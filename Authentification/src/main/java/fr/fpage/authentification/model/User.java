package fr.fpage.authentification.model;

import fr.fpage.authentification.Application;
import fr.fpage.authentification.manager.GroupManager;
import fr.fpage.authentification.requests.UserCreateRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * CREATE TABLE `auth`.`users`
 * ( `uuid` VARCHAR(40) NOT NULL ,
 *   `username` VARCHAR(15),
 *   `email` VARCHAR(100) NOT NULL ,
 *   `password` VARCHAR(15) NOT NULL ,
 *   `groupUuid` VARCHAR(40) NOT NULL ,
 *   `createdDate` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
 *   PRIMARY KEY (`uuid`), UNIQUE (`username`), UNIQUE (`email`));
 *
 *   CREATE TABLE `auth`.`userPremissions`
 *   ( `id` INT NOT NULL AUTO_INCREMENT ,
 *   `userUuid` VARCHAR(40) NOT NULL ,
 *   `PermissionNode` VARCHAR(200) NOT NULL ,
 *   PRIMARY KEY (`id`))
 */

public class User {

    private UUID uuid;
    private String username, email, password;
    private Date creationDate;
    private UserStatus userStatus = UserStatus.LOADING;
    private Group group;
    private List<String> userPermissions = new ArrayList<>();

    public User(UserCreateRequest user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public User(UUID uuid) {
        this.uuid = uuid;
        this.loadUuid();
        this.loadPermissions();
    }

    private void loadUuid() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `users` WHERE `uuid`=?");
            preparedStatement.setString(1, this.uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.email = res.getString("email");
                this.username = res.getString("username");
                try
                {
                        this.group = GroupManager.getInstance().getGroup(UUID.fromString(res.getString("groupUuid")));
                }
                catch (NullPointerException | IllegalArgumentException ignore)
                {
                    this.group = null;
                }
                this.creationDate = res.getDate("createdDate");
                this.userStatus = UserStatus.OK;
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
    }

    public User createUser()
    {
        if (this.checkValues()) {
            try {
                Connection connection = Application.getDb().getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `users` (`uuid`, `username`, `email`, `password`, `groupUuid`) VALUES (?, ?, ?, ?, ?)");
                this.uuid = UUID.randomUUID();
                preparedStatement.setString(1, this.uuid.toString());
                if (this.username.equals(""))
                    preparedStatement.setString(2, null);
                else
                    preparedStatement.setString(2, this.username);
                preparedStatement.setString(3, this.email);
                preparedStatement.setString(4, this.password);
                preparedStatement.setString(5, null);
                preparedStatement.execute();
                connection.close();
            } catch (SQLIntegrityConstraintViolationException e1) {
                if (e1.getMessage().contains("email")) {
                    this.userStatus = UserStatus.ERROR_ALREADYUSED_EMAIL;
                    return this;
                }
                if (e1.getMessage().contains("username")) {
                    this.userStatus = UserStatus.ERROR_ALREADYUSED_USERNAME;
                    return this;
                }
                System.out.println(e1.getMessage());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.userStatus = UserStatus.OK;
        }
        return this;
    }

    private boolean checkValues() {
        if (!this.username.equalsIgnoreCase("") && this.username.length() < 3) {
            this.userStatus = UserStatus.ERROR_FORM_USERNAME;
            return false;
        }
        if (!this.checkEmailRules(this.email))
        {
            this.userStatus = UserStatus.ERROR_FORM_EMAIL;
            return false;
        }
        if (this.password.length() < 5)
        {
            this.userStatus = UserStatus.ERROR_FORM_PASSWORD;
            return false;
        }
        return true;
    }

    public UUID getUuid() {
        return uuid;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void connect() {
        if (username != null) {
            if (this.checkEmailRules(username)) {
                this.loadUserEmailPassword();
            }
            else
            {
                this.loadUserNamePassword();
            }
            this.loadPermissions();
        }
    }

    private void loadPermissions() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `node` FROM `userpermissions` WHERE `userUuid`=?");
            preparedStatement.setString(1, this.uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.userPermissions.add(res.getString("node"));
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
    }

    private void loadUserNamePassword() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `users` WHERE `username`=? AND `password`=?");
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.password);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.uuid = UUID.fromString(res.getString("uuid"));
                this.email = res.getString("email");
                this.group = GroupManager.getInstance().getGroup(UUID.fromString(res.getString("groupUuid")));
                this.creationDate = res.getDate("createdDate");
                this.userStatus = UserStatus.OK;
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
    }

    private void loadUserEmailPassword() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `users` WHERE `email`=? AND `password`=?");
            preparedStatement.setString(1, this.username);
            preparedStatement.setString(2, this.password);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.uuid = UUID.fromString(res.getString("uuid"));
                this.email = this.username;
                this.username = res.getString("username");
                this.group = GroupManager.getInstance().getGroup(UUID.fromString(res.getString("groupUuid")));
                this.creationDate = res.getDate("createdDate");
                this.userStatus = UserStatus.OK;
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
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

    public List<String> getUserPermissions() {
        List<String> nodes = new ArrayList<>(this.userPermissions);
        try {
            nodes.addAll(this.group.getPermissionsNodes());
        }
        catch (NullPointerException ignored) {}
        return nodes;
    }

    public boolean hasPermission(String permission) {
        return this.userPermissions.contains(permission);
    }

    public enum UserStatus
    {
        LOADING,
        ERROR_FORM_EMAIL,
        ERROR_ALREADYUSED_EMAIL,
        ERROR_FORM_USERNAME,
        ERROR_ALREADYUSED_USERNAME,
        ERROR_FORM_PASSWORD,
        OK
    }
}
