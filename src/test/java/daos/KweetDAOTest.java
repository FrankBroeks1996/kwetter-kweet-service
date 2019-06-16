package daos;

import models.Kweet;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Test;

import javax.ejb.EJB;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

public class KweetDAOTest {

    @EJB
    private IKweetDAO kweetDAO;

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void addKweet(){
        Kweet kweet = new Kweet();
        kweet.setMessage("TestKweet");
        kweetDAO.addKweet(kweet);

        Kweet kweet1 = kweetDAO.getKweetById(kweet.getId());
        assertEquals("TestKweet", kweet1.getMessage());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void editKweet(){
        Kweet kweet = new Kweet();
        kweet.setMessage("TestKweet");
        kweetDAO.addKweet(kweet);

        Kweet kweet1 = new Kweet();
        kweet1.setId(kweet.getId());
        kweet1.setMessage("EditedKweet");
        kweetDAO.editKweet(kweet1);

        assertEquals("EditedKweet", kweet.getMessage());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void removeKweet(){
        Kweet kweet = new Kweet();
        kweet.setMessage("TestKweet");
        kweetDAO.addKweet(kweet);

        assertNotNull(kweetDAO.getKweetById(kweet.getId()));

        kweetDAO.removeKweet(kweet);

        assertNull(kweetDAO.getKweetById(kweet.getId()));
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getKweetById(){
        Kweet kweet = new Kweet();
        kweet.setMessage("TestKweet");
        kweetDAO.addKweet(kweet);

        Kweet kweet1 = kweetDAO.getKweetById(kweet.getId());

        assertEquals(kweet.getId(), kweet1.getId());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void heartKweet(){
        Kweet kweet = new Kweet();
        kweet.setMessage("TestKweet");
        kweetDAO.addKweet(kweet);

        assertEquals(0, kweetDAO.getKweetById(kweet.getId()).getHearts().size());

        kweetDAO.heartKweet(UUID.randomUUID(), kweet);

        assertEquals(1, kweetDAO.getKweetById(kweet.getId()).getHearts().size());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getAllUserKweets(){
        UUID author = UUID.randomUUID();

        Kweet kweet1 = new Kweet();
        kweet1.setMessage("TestKweet1");
        kweet1.setAuthor(author);
        kweetDAO.addKweet(kweet1);

        Kweet kweet2 = new Kweet();
        kweet2.setMessage("TestKweet2");
        kweet2.setAuthor(author);
        kweetDAO.addKweet(kweet2);

        Kweet kweet3 = new Kweet();
        kweet3.setMessage("TestKweet3");
        kweet3.setAuthor(UUID.randomUUID());
        kweetDAO.addKweet(kweet3);

        assertEquals(2, kweetDAO.getAllUserKweets(author).size());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getDashboard(){
        UUID user = UUID.randomUUID();
        UUID userFollowing = UUID.randomUUID();
        List<UUID> following = new ArrayList<>();
        following.add(userFollowing);

        Kweet kweet1 = new Kweet();
        kweet1.setMessage("TestKweet1");
        kweet1.setAuthor(user);
        kweetDAO.addKweet(kweet1);

        Kweet kweet2 = new Kweet();
        kweet2.setMessage("TestKweet2");
        kweet2.setAuthor(userFollowing);
        kweetDAO.addKweet(kweet2);

        Kweet kweet3 = new Kweet();
        kweet3.setMessage("TestKweet3");
        kweet3.setAuthor(userFollowing);
        kweetDAO.addKweet(kweet3);

        Kweet kweet4 = new Kweet();
        kweet3.setMessage("TestKweet4");
        kweet3.setAuthor(userFollowing);
        kweetDAO.addKweet(kweet4);

        assertEquals(3, kweetDAO.getDashboard(user, following, 1 , 10).size());
    }

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void getSearchResult(){
        Kweet kweet1 = new Kweet();
        kweet1.setMessage("TestKweet1");
        kweetDAO.addKweet(kweet1);

        Kweet kweet2 = new Kweet();
        kweet2.setMessage("TestKweet2");
        kweetDAO.addKweet(kweet2);

        Kweet kweet3 = new Kweet();
        kweet3.setMessage("Test3");
        kweetDAO.addKweet(kweet3);

        assertEquals(2, kweetDAO.getSearchResult("Kweet", 1, 10).size());
    }
}
