package fr.fpage.authentification.manager;

import fr.fpage.authentification.model.Client;
import fr.fpage.authentification.requests.ClientCreateRequest;

import java.util.HashMap;
import java.util.UUID;

public class ClientManager {
    private static ClientManager ourInstance = new ClientManager();

    public static ClientManager getInstance() {
        return ourInstance;
    }

    private HashMap<UUID, Client> clients = new HashMap<>();

    private ClientManager() {
    }

    public Client getClient(UUID uuid)
    {
        if (!this.clients.containsKey(uuid))
            this.clients.put(uuid, new Client(uuid));
        return this.clients.get(uuid);
    }

    public Client createClient(ClientCreateRequest client) {
        return new Client(client);
    }
}
