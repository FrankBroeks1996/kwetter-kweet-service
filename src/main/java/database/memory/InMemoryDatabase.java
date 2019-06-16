package database.memory;

import models.Kweet;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InMemoryDatabase {
    private static InMemoryDatabase inMemoryDatabase;

    protected List<Kweet> kweets;

    public InMemoryDatabase(){
        cleanUp();
    }

    public static InMemoryDatabase getInMemoryDatabase(){
        if(inMemoryDatabase == null){
            inMemoryDatabase = new InMemoryDatabase();
        }
        return inMemoryDatabase;
    }

    public void cleanUp(){
        this.kweets = new ArrayList<>();
    }

    public List<Kweet> getKweets(){ return kweets; }

    public Kweet getKweetById(UUID kweetId){
        return kweets.stream().filter(k -> k.getId() == kweetId).findAny().orElse(null);
    }

    public Kweet addKweet(Kweet kweet){
        kweet.setId(UUID.randomUUID());
        kweet.setHearts(new ArrayList<>());
        kweets.add(kweet);
        return kweet;
    }

    public void removeKweet(Kweet kweet){
        kweets.removeIf(k -> k.getId() == kweet.getId());
    }
}
