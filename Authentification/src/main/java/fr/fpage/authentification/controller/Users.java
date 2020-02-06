package fr.fpage.authentification.controller;

import fr.fpage.authentification.exception.TokenNotExistException;
import fr.fpage.authentification.manager.*;
import fr.fpage.authentification.model.User;
import fr.fpage.authentification.model.UserToken;
import fr.fpage.authentification.requests.RefreshTokenRequest;
import fr.fpage.authentification.requests.UserCreateRequest;
import fr.fpage.authentification.requests.UserConnectRequest;
import fr.fpage.authentification.requests.UserTokenResolveRequest;
import fr.fpage.authentification.responses.UserEntityResponse;
import fr.fpage.authentification.responses.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class Users {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/users/create", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> createUser(@RequestHeader(value="Authorization") String authorization,
                                                   @RequestBody UserCreateRequest user)
    {
        System.out.println(authorization);
        if (ClientManager.getInstance().getClient(UUID.fromString(authorization)).getPermissionLevel() < 10)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User userModel = UserManager.getInstance().createUser(user);
        return new ResponseEntity<>(new UserResponse(userModel.getUserStatus().toString(), userModel.getUuid()),
                HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/users/connect", method = RequestMethod.POST)
    public ResponseEntity<UserResponse> connectUser(@RequestHeader(value="authorization") String authorization,
                                                    @RequestBody UserConnectRequest user)
    {
        if (ClientManager.getInstance().getClient(UUID.fromString(authorization)).getPermissionLevel() < 10)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        UUID userUuid;
        if ((userUuid = user.connect()) == null)
            return new ResponseEntity<>(new UserResponse("ERROR", null), HttpStatus.OK);
        else {
            try {
                return new ResponseEntity<>(new UserResponse("SUCCESS", RefreshTokenManager.getInstance()
                        .createRefreshToken(
                        new RefreshTokenRequest(UUID.fromString(authorization), UserTokenManager.getInstance()
                                .createUserToken(UUID.fromString(authorization),
                                        userUuid).getToken())).getToken()), HttpStatus.OK);
            } catch (TokenNotExistException e) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        }
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/users/other", method = RequestMethod.GET)
    public ResponseEntity<UserEntityResponse> getUserOther(@RequestHeader(value="authorization") String userToken,
                                                      @RequestParam String userSearchUuid)
    {
        UserTokenResolveRequest request = new UserTokenResolveRequest(userToken);
        UserToken token = request.getUserToken();
        if (token == null && !token.getUser().hasPermission("users.get"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User user = UserManager.getInstance().getUser(userSearchUuid);
        return new ResponseEntity<>(new UserEntityResponse(user.getUuid(), user.getUsername(), user.getEmail(),
                user.getUserPermissions()), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/users/self", method = RequestMethod.GET)
    public ResponseEntity<UserEntityResponse> getUserSelf(@RequestHeader(value="authorization") String userToken)
    {
        UserTokenResolveRequest request = new UserTokenResolveRequest(userToken);
        UserToken token = request.getUserToken();
        if (token == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        User user = token.getUser();
        return new ResponseEntity<>(new UserEntityResponse(user.getUuid(), user.getUsername(), user.getEmail(),
                user.getUserPermissions()), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/users/list", method = RequestMethod.GET)
    public ResponseEntity<User[]> getAllUsers(@RequestHeader(value = "authorization") String userToken)
    {
        UserTokenResolveRequest request = new UserTokenResolveRequest(userToken);
        UserToken token = request.getUserToken();
        if (token == null || !token.getUser().hasPermission("users.get"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(UserManager.getUsers(), HttpStatus.OK);
    }
}
