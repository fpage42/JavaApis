package fr.fpage.authentification.controller;

import fr.fpage.authentification.manager.ClientManager;
import fr.fpage.authentification.model.Client;
import fr.fpage.authentification.requests.ClientCreateRequest;
import fr.fpage.authentification.responses.ClientInformationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class Clients {

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/clients", method = RequestMethod.PUT)
    public ResponseEntity<String> CreateClient(@RequestHeader(value="authorization") String authorization, @RequestBody ClientCreateRequest client)
    {
        if (ClientManager.getInstance().getClient(UUID.fromString(authorization)).getPermissionLevel() < 10)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Client clientModel = ClientManager.getInstance().createClient(client);
        return new ResponseEntity<>(clientModel.getUuid().toString(), HttpStatus.OK);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public ResponseEntity<ClientInformationResponse> CreateClient(@RequestHeader(value="authorization") String authorization, @RequestParam String clientId)
    {
        if (ClientManager.getInstance().getClient(UUID.fromString(authorization)).getPermissionLevel() < 10)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Client client = ClientManager.getInstance().getClient(UUID.fromString(clientId));
        if (client == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(new ClientInformationResponse(client.getRedirectUrl()), HttpStatus.OK);
    }


}
