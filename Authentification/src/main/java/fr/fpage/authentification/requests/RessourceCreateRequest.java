package fr.fpage.authentification.requests;

public class RessourceCreateRequest {

    private String name;

    public RessourceCreateRequest()
    {}

    public RessourceCreateRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
