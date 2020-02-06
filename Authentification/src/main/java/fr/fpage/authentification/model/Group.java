package fr.fpage.authentification.model;

import fr.fpage.authentification.Application;
import fr.fpage.authentification.requests.GroupCreateRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*
* CREATE TABLE `auth`.`groups` ( `uuid` VARCHAR(40) NOT NULL , `name` VARCHAR(40) NOT NULL , UNIQUE (`uuid`))
* CREATE TABLE `auth`.`groupPermissions` ( `id` INT NOT NULL AUTO_INCREMENT , `groupUuid` VARCHAR(40) NOT NULL , `node` VARCHAR(200) NOT NULL , PRIMARY KEY (`id`))
 */

public class Group {

    private UUID uuid;
    private String name;
    private List<String> permissionsNodes = new ArrayList<>();

    public Group(UUID uuid)
    {
        this.uuid = uuid;
        this.loadGroup();
        this.loadPermissions();
    }

    public Group(GroupCreateRequest groupCreateRequest) {
        this.name = groupCreateRequest.getName();
        this.uuid = UUID.randomUUID();
        this.createGroup();
    }

    private void createGroup() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `groups` (`uuid`, `name`) VALUES (?, ?)");
            preparedStatement.setString(1, this.uuid.toString());
            preparedStatement.setString(2, this.name);
            preparedStatement.execute();
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
    }

    private void loadGroup() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM `groups` WHERE `uuid`=?");
            preparedStatement.setString(1, this.uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.name = res.getString("name");
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }

    }

    private void loadPermissions() {
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `node` FROM `groupPermissions` WHERE `groupUuid`=?");
            preparedStatement.setString(1, this.uuid.toString());
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                this.permissionsNodes.add(res.getString("node"));
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
    }

    public void addPermissionNode(String permissionNode)
    {
        this.permissionsNodes.add(permissionNode);
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO `groupPermissions` (`groupUuid`, `node`) VALUES (?, ?)");
            preparedStatement.setString(1, this.uuid.toString());
            preparedStatement.setString(2, permissionNode);
            preparedStatement.execute();
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
    }

    public void removePermissionNode(String permissionNode)
    {
        this.permissionsNodes.remove(permissionNode);
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `groupPermissions` where `groupUuid`=? AND `node`=?");
            preparedStatement.setString(1, this.uuid.toString());
            preparedStatement.setString(2, permissionNode);
            preparedStatement.execute();
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissionsNodes() {
        return permissionsNodes;
    }
}
