package fr.fpage.authentification.controller;

import fr.fpage.authentification.exception.TokenNotExistException;
import fr.fpage.authentification.manager.ClientManager;
import fr.fpage.authentification.manager.RefreshTokenManager;
import fr.fpage.authentification.manager.UserTokenManager;
import fr.fpage.authentification.model.RefreshToken;
import fr.fpage.authentification.model.UserToken;
import fr.fpage.authentification.requests.RefreshTokenRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class Tokens {

    private static final String REDIRECT_URL_CREATE_REFRESH_TOKEN = "localhost:4200/login/";

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/tokens/user", method = RequestMethod.GET)
    public ResponseEntity<String> getUserToken(@RequestHeader(value="authorization") String authorization, @RequestParam String refreshToken)
    {
        System.out.println("Route user token");
        if (refreshToken == null || refreshToken.equals(""))
            return new ResponseEntity<>((HttpStatus.BAD_REQUEST));
        if (ClientManager.getInstance().getClient(UUID.fromString(authorization)).getPermissionLevel() < 1)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        try {
            RefreshToken refreshTokenObject = RefreshTokenManager.getInstance().getRefreshToken(UUID.fromString(refreshToken));
            if (refreshTokenObject.getClientUuid().equals(UUID.fromString(authorization))) {
                UserToken userToken = UserTokenManager.getInstance().createUserToken(UUID.fromString(authorization), refreshTokenObject.getUserUuid());
                return new ResponseEntity<>(userToken.getToken().toString(), HttpStatus.OK);
            }
        } catch (TokenNotExistException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/tokens/refresh", method = RequestMethod.GET)
    public ResponseEntity<String> getRefreshToken(@RequestHeader(value="authorization") String authorization, @RequestParam String clientId, String userTokenId)
    {
        System.out.println("Route refresh token");
        try {
            UUID clientUuid = RefreshTokenManager.getInstance().getRefreshToken(UUID.fromString(authorization)).getClientUuid();
            if (ClientManager.getInstance().getClient(clientUuid).getPermissionLevel() < 10)
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (TokenNotExistException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        RefreshToken refreshToken = null;
        try {
            refreshToken = RefreshTokenManager.getInstance().createRefreshToken(new RefreshTokenRequest(UUID.fromString(clientId), UUID.fromString(userTokenId)));
        } catch (TokenNotExistException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(refreshToken.getToken().toString(), HttpStatus.OK);
    }


}
