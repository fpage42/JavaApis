package fr.fpage.authentification.controller;

import fr.fpage.authentification.exception.TokenNotExistException;
import fr.fpage.authentification.manager.GroupManager;
import fr.fpage.authentification.manager.UserTokenManager;
import fr.fpage.authentification.model.Group;
import fr.fpage.authentification.model.UserToken;
import fr.fpage.authentification.requests.GroupCreateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Groups {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/groups", method = RequestMethod.GET)
    public ResponseEntity<Group[]> getAllGroups(@RequestHeader(value="authorization") String userToken)
    {
        UserToken token;
        try {
            token = UserTokenManager.getInstance().getUserToken(userToken);
        } catch (TokenNotExistException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!token.getUser().hasPermission("groups.get.all"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(GroupManager.getInstance().getAllGroups(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/group", method = RequestMethod.PUT)
    public ResponseEntity<Group> createGroup(@RequestHeader(value="authorization") String userToken,
                                               @RequestBody GroupCreateRequest groupCreateRequest)
    {
        UserToken token;
        try {
            token = UserTokenManager.getInstance().getUserToken(userToken);
        } catch (TokenNotExistException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (!token.getUser().hasPermission("groups.create"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(GroupManager.getInstance().createGroup(groupCreateRequest), HttpStatus.OK);
    }

}
