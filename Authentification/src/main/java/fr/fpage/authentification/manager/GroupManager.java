package fr.fpage.authentification.manager;

import fr.fpage.authentification.Application;
import fr.fpage.authentification.requests.GroupCreateRequest;
import fr.fpage.authentification.model.Group;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GroupManager {
    private static GroupManager ourInstance = new GroupManager();

    public static GroupManager getInstance() {
        return ourInstance;
    }

    private HashMap<UUID, Group> groups = new HashMap<>();

    private GroupManager() {
    }

    public Group getGroup(UUID uuid)
    {
        if (!this.groups.containsKey(uuid))
            this.groups.put(uuid, new Group(uuid));
        return this.groups.get(uuid);
    }

    public Group createGroup(GroupCreateRequest groupCreateRequest)
    {
        return new Group(groupCreateRequest);
    }

    public Group[] getAllGroups() {
        List<UUID> uuids = new ArrayList<>();
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT uuid FROM `groups`");
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                uuids.add(UUID.fromString(res.getString("uuid")));
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
        Group[] groups = uuids.stream().map(Group::new).toArray(Group[]::new);
        return groups;
    }
}
