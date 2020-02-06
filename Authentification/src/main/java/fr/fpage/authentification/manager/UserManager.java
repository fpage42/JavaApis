package fr.fpage.authentification.manager;

import fr.fpage.authentification.Application;
import fr.fpage.authentification.model.User;
import fr.fpage.authentification.requests.UserCreateRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UserManager {

    private static UserManager ourInstance = new UserManager();

    public static UserManager getInstance() {
        return ourInstance;
    }

    private HashMap<UUID, User> users = new HashMap<>();

    private UserManager() {}

    public static User[] getUsers() {

        List<UUID> uuids = new ArrayList<>();
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT uuid FROM `users`");
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

        User[] users = uuids.stream().map(User::new).toArray(User[]::new);
        return users;
    }

    public User getUser(String uuid)
    {
        return this.getUser(UUID.fromString(uuid));
    }

    public User getUser(UUID uuid)
    {
        if (!this.users.containsKey(uuid))
            this.users.put(uuid, new User(uuid));
        return this.users.get(uuid);
    }

    public void setUser(User user)
    {
        this.users.put(user.getUuid(), user);
    }

    public User createUser(UserCreateRequest user) {
        return new User(user).createUser();
    }
}
