package fr.fpage.authentification.controller;

import fr.fpage.authentification.exception.TokenNotExistException;
import fr.fpage.authentification.manager.ClientManager;
import fr.fpage.authentification.manager.RessourceManager;
import fr.fpage.authentification.manager.UserManager;
import fr.fpage.authentification.manager.UserTokenManager;
import fr.fpage.authentification.model.Ressource;
import fr.fpage.authentification.model.UserToken;
import fr.fpage.authentification.requests.RessourceCreateRequest;
import fr.fpage.authentification.responses.RessourceResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class Ressources {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/ressources", method = RequestMethod.PUT)
    public ResponseEntity<RessourceResponse> createRessource(@RequestHeader(value="authorization") String authorization, @RequestBody RessourceCreateRequest request)
    {
        try {
            UserToken userToken = UserTokenManager.getInstance().getUserToken(UUID.fromString(authorization));
            if (ClientManager.getInstance().getClient(userToken.getClientUuid()).getPermissionLevel() < 10)
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            if (!UserManager.getInstance().getUser(userToken.getUserUuid()).getUserPermissions().contains("authentification.ressource.create"))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            Ressource ressource = RessourceManager.getInstance().createRessource(request, userToken.getUserUuid());
            return new ResponseEntity<>(new RessourceResponse(ressource.getUuid()), HttpStatus.OK);
        } catch (TokenNotExistException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
  //      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
