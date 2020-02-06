package fr.fpage.conservatoireuserapi.controllers;

import fr.fpage.conservatoireuserapi.authentification.Authentification;
import fr.fpage.conservatoireuserapi.authentification.UserToken;
import fr.fpage.conservatoireuserapi.exceptions.UserLoadException;
import fr.fpage.conservatoireuserapi.managers.UsersManager;
import fr.fpage.conservatoireuserapi.models.ReduceUser;
import fr.fpage.conservatoireuserapi.models.User;
import fr.fpage.conservatoireuserapi.requests.ChangeUserDatasRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class Users {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/user/other", method = RequestMethod.GET)
    public ResponseEntity<User> getUser(@RequestHeader(value="authorization") String authorization, @RequestParam String userUuid)
    {
        User user = null;
        try {
            user = UsersManager.getInstance().getUser(Authentification.getAuthUser(new UserToken(authorization), userUuid));
        } catch (UserLoadException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/user/self", method = RequestMethod.GET)
    public ResponseEntity<User> getSelfDatas(@RequestHeader(value="authorization") String authorization)
    {
        User user = null;
        try {
            user = UsersManager.getInstance().getUser(Authentification.getAuthUser(new UserToken(authorization)));
        } catch (UserLoadException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity insertUser(@RequestHeader(value="Authorization") String authorization)
    {
/*        AuthUser authUser = Authentification.getAuthUser(new UserToken(UUID.fromString(authorization)));
        UsersManager.getInstance().createUser(authUser);*/
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/user/datas/other", method = RequestMethod.POST)
    public ResponseEntity changeDatas(@RequestHeader(value="Authorization") String authorization, @RequestParam String userUuid, @RequestBody ChangeUserDatasRequest request)
    {
        System.out.println("change datas");
        User user;
        try {
            user = this.getUserObject(authorization);
            if (!user.hasPermission("user.modify"))
                return new ResponseEntity(HttpStatus.UNAUTHORIZED);
            UsersManager.getInstance().getUser(UUID.fromString(userUuid)).changeDatas(request);
        } catch (UserLoadException e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ResponseEntity<ReduceUser[]> getAllUsers(@RequestHeader(value="Authorization") String authorization)
    {
        return new ResponseEntity<>(UsersManager.getReduceUserList(new UserToken(UUID.fromString(authorization))), HttpStatus.OK);
    }


    private User getUserObject(String authorization) throws UserLoadException {
        return UsersManager.getInstance().getUser(Authentification.getAuthUser(new UserToken(UUID.fromString(authorization))));
    }
}
