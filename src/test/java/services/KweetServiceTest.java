package services;

import daos.KweetDAOMemoryImpl;
import database.memory.InMemoryDatabase;
import models.Kweet;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class KweetServiceTest {

    private KweetService kweetService;

    @Before
    public void setup(){
        kweetService = new KweetService();
        kweetService.setKweetDAO(new KweetDAOMemoryImpl());
    }

    @After
    public void cleanUp(){
        InMemoryDatabase.getInMemoryDatabase().cleanUp();
    }

    @Test
    public void postKweet(){
        Kweet kweet = new Kweet();
        kweet.setMessage("TestKweet");
        UUID author = UUID.randomUUID();

        kweetService.postKweet(author, kweet);

        Kweet kweet1 = InMemoryDatabase.getInMemoryDatabase().getKweetById(kweet.getId());
        assertEquals(author, kweet1.getAuthor());
        assertEquals(kweet.getMessage(), kweet1.getMessage());
    }

    @Test
    public void heartKweet(){
        Kweet kweet = new Kweet();
        kweet.setMessage("TestKweet");
        UUID author = UUID.randomUUID();

        kweetService.postKweet(author, kweet);

        Kweet kweet1 = InMemoryDatabase.getInMemoryDatabase().getKweetById(kweet.getId());

        assertEquals(0, kweet1.getHearts().size());

        kweetService.heartKweet(UUID.randomUUID(), kweet);

        assertEquals(1, kweet1.getHearts().size());
    }

    @Test
    public void removeKweet(){
        Kweet kweet = new Kweet();
        kweet.setMessage("TestKweet");
        UUID author = UUID.randomUUID();

        kweetService.postKweet(author, kweet);

        assertNotNull(InMemoryDatabase.getInMemoryDatabase().getKweetById(kweet.getId()));

        kweetService.removeKweet(kweet);

        assertNull(InMemoryDatabase.getInMemoryDatabase().getKweetById(kweet.getId()));
    }

    @Test
    public void getAllUserKweets(){
        UUID user = UUID.randomUUID();

        Kweet kweet1 = new Kweet();
        kweet1.setMessage("TestKweet");
        kweetService.postKweet(user, kweet1);

        Kweet kweet2 = new Kweet();
        kweet2.setMessage("TestKweet");
        kweetService.postKweet(user, kweet2);

        Kweet kweet3 = new Kweet();
        kweet3.setMessage("TestKweet");
        kweetService.postKweet(UUID.randomUUID(), kweet3);

        assertEquals(2, kweetService.getAllUserKweets(user).size());
    }

    @Test
    public void getDashboard(){
        UUID user = UUID.randomUUID();
        UUID userFollowing = UUID.randomUUID();
        List<UUID> following = new ArrayList<>();
        following.add(userFollowing);

        Kweet kweet1 = new Kweet();
        kweet1.setMessage("TestKweet");
        kweetService.postKweet(user, kweet1);

        Kweet kweet2 = new Kweet();
        kweet2.setMessage("TestKweet");
        kweetService.postKweet(user, kweet2);

        Kweet kweet3 = new Kweet();
        kweet3.setMessage("TestKweet");
        kweetService.postKweet(userFollowing, kweet3);

        Kweet kweet4 = new Kweet();
        kweet4.setMessage("TestKweet");
        kweetService.postKweet(UUID.randomUUID(), kweet4);

        assertEquals(3, kweetService.getDashboard(user, following, 1, 10).size());
    }

    @Test
    public void getSearchResult(){
        Kweet kweet1 = new Kweet();
        kweet1.setMessage("TestKweet1");
        kweetService.postKweet(UUID.randomUUID(), kweet1);

        Kweet kweet2 = new Kweet();
        kweet2.setMessage("TestKweet2");
        kweetService.postKweet(UUID.randomUUID(), kweet2);

        Kweet kweet3 = new Kweet();
        kweet3.setMessage("Test3");
        kweetService.postKweet(UUID.randomUUID(), kweet3);

        assertEquals(2, kweetService.getSearchResult("Kweet", 1, 10).size());
    }
}
