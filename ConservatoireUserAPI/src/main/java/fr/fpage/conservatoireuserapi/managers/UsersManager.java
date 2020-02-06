package fr.fpage.conservatoireuserapi.managers;

import fr.fpage.conservatoireuserapi.authentification.AuthUser;
import fr.fpage.conservatoireuserapi.authentification.Authentification;
import fr.fpage.conservatoireuserapi.authentification.UserToken;
import fr.fpage.conservatoireuserapi.exceptions.UserLoadException;
import fr.fpage.conservatoireuserapi.models.ReduceUser;
import fr.fpage.conservatoireuserapi.models.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class UsersManager {
    private static UsersManager ourInstance = new UsersManager();

    public static UsersManager getInstance() {
        return ourInstance;
    }

    private HashMap<UUID, User> users = new HashMap<>();

    private UsersManager() {
    }

    public void createUser(AuthUser authUser)
    {
        new User(authUser.getUuid());
    }

    public User getUser(AuthUser authUser) throws UserLoadException {
        if (!users.containsKey(authUser.getUuid()))
            users.put(authUser.getUuid(), new User(authUser));
        return users.get(authUser.getUuid());
    }

    public User getUser(UUID userUuid) throws UserLoadException {
        if (!users.containsKey(userUuid))
            throw new UserLoadException(UserLoadException.UserExceptionReasons.UUID_NOT_FOUND);
        return users.get(userUuid);
    }

    public static ReduceUser[] getReduceUserList(UserToken userToken)
    {
        AuthUser[] authUsers = Authentification.getAuthUserList(userToken);
        ReduceUser[] reduceUsers = Arrays.stream(authUsers).map(ReduceUser::new).toArray(ReduceUser[]::new);
        return reduceUsers;
    }
}
