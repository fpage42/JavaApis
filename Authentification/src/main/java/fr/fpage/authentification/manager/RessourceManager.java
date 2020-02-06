package fr.fpage.authentification.manager;

import fr.fpage.authentification.model.Ressource;
import fr.fpage.authentification.requests.RessourceCreateRequest;

import java.util.HashMap;
import java.util.UUID;

public class RessourceManager {
    private static RessourceManager ourInstance = new RessourceManager();

    public static RessourceManager getInstance() {
        return ourInstance;
    }

    private HashMap<UUID, Ressource> ressources = new HashMap<>();

    private RessourceManager() {
    }

    public Ressource getRessource(UUID uuid)
    {
        if (!ressources.containsKey(uuid))
            ressources.put(uuid, new Ressource(uuid));
        return ressources.get(uuid);
    }

    public Ressource createRessource(RessourceCreateRequest ressourceCreateRequest, UUID userUuid)
    {
        return new Ressource(ressourceCreateRequest, userUuid);
    }
}
