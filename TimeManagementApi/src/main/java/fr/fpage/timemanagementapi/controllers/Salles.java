package fr.fpage.timemanagementapi.controllers;

import fr.fpage.timemanagementapi.authentification.AuthUser;
import fr.fpage.timemanagementapi.authentification.Authentification;
import fr.fpage.timemanagementapi.authentification.UserToken;
import fr.fpage.timemanagementapi.exceptions.BddLoadException;
import fr.fpage.timemanagementapi.managers.SallesManager;
import fr.fpage.timemanagementapi.models.Salle;
import fr.fpage.timemanagementapi.requests.CreateSalleRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class Salles {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/salles", method = RequestMethod.PUT)
    public ResponseEntity<Salle> createSalle(@RequestHeader(value = "authorization") String authorization,
                                             @RequestBody CreateSalleRequest createSalleRequest)
    {
        AuthUser authUser = Authentification.getAuthUser(new UserToken(authorization));
        if (authUser.hasPermission("salles.create"))
        {
            return new ResponseEntity<>(SallesManager.getInstance().createSalle(createSalleRequest), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/salles", method = RequestMethod.GET)
    public ResponseEntity<Salle> getSalle(@RequestHeader(value = "authorization") String authorization,
                                          @RequestParam String uuid)
    {
        AuthUser authUser = Authentification.getAuthUser(new UserToken(authorization));
        if (authUser.hasPermission("salles.get"))
        {
            try {
                return new ResponseEntity<>(SallesManager.getInstance().getSalle(UUID.fromString(uuid)), HttpStatus.OK);
            } catch (BddLoadException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/salles/all", method = RequestMethod.GET)
    public ResponseEntity<Salle[]> getAllSalle(@RequestHeader(value = "authorization") String authorization)
    {
        AuthUser authUser = Authentification.getAuthUser(new UserToken(authorization));
        if (authUser.hasPermission("salles.get"))
        {
            return new ResponseEntity<>(SallesManager.getAllSalles(), HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
