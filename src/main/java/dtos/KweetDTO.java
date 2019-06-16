package dtos;

import models.Kweet;

import java.util.UUID;

public class KweetDTO {

    private UUID id;
    private String message;
    private UserDTO author;

    public KweetDTO(){}

    public KweetDTO(Kweet kweet){
        this.id = kweet.getId();
        this.message = kweet.getMessage();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public void setAuthor(UserDTO user) {
        this.author = user;
    }
}
