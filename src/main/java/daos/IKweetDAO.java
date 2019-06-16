package daos;

import dtos.UserDTO;
import models.Kweet;

import java.util.List;
import java.util.UUID;

public interface IKweetDAO {
    void addKweet(Kweet kweet);

    void editKweet(Kweet kweet);

    void removeKweet(Kweet kweet);

    Kweet getKweetById(UUID id);

    void heartKweet(UUID user, Kweet kweet);

    List<Kweet> getAllUserKweets(UUID user);

    List<Kweet> getDashboard(UUID user, List<UUID> following, int resultPage, int resultSize);

    List<Kweet> getSearchResult(String searchQuery, int resultPage, int resultSize);
}
