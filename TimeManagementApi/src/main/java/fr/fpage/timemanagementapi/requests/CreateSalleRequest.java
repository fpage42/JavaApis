package fr.fpage.timemanagementapi.requests;

public class CreateSalleRequest {

    public CreateSalleRequest() {
    }

    public CreateSalleRequest(String name, int size) {
        this.name = name;
        this.size = size;
    }

    private String name;
    private int size;

    public String getName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
