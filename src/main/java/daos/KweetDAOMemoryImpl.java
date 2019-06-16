package daos;

import database.memory.InMemoryDatabase;
import models.Kweet;

import javax.ejb.Stateless;
import javax.enterprise.inject.Alternative;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Stateless
@Alternative
public class KweetDAOMemoryImpl implements IKweetDAO {

    @Override
    public void addKweet(Kweet kweet) {
        InMemoryDatabase.getInMemoryDatabase().addKweet(kweet);
    }

    @Override
    public void editKweet(Kweet kweet) {
        Kweet savedKweet = InMemoryDatabase.getInMemoryDatabase().getKweetById(kweet.getId());
        savedKweet.setMessage(kweet.getMessage());
    }

    @Override
    public void removeKweet(Kweet kweet) {
        InMemoryDatabase.getInMemoryDatabase().removeKweet(kweet);
    }

    @Override
    public Kweet getKweetById(UUID id) {
        return InMemoryDatabase.getInMemoryDatabase().getKweetById(id);
    }

    @Override
    public void heartKweet(UUID user, Kweet kweet) {
        Kweet savedKweet = InMemoryDatabase.getInMemoryDatabase().getKweetById(kweet.getId());
        savedKweet.getHearts().add(user);
    }

    @Override
    public List<Kweet> getAllUserKweets(UUID user) {
        return InMemoryDatabase.getInMemoryDatabase().getKweets().stream().filter(k -> k.getAuthor() == user).collect(Collectors.toList());
    }

    @Override
    public List<Kweet> getDashboard(UUID user, List<UUID> following, int resultPage, int resultSize) {
        return InMemoryDatabase.getInMemoryDatabase()
                .getKweets()
                .stream()
                .filter(k -> k.getAuthor() == user || following.stream().anyMatch(f -> f == k.getAuthor()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Kweet> getSearchResult(String searchQuery, int resultPage, int resultSize) {
        return InMemoryDatabase.getInMemoryDatabase()
                .getKweets()
                .stream()
                .filter(k -> k.getMessage().contains(searchQuery))
                .collect(Collectors.toList());
    }
}
