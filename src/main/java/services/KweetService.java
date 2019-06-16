package services;

import daos.IKweetDAO;
import dtos.UserDTO;
import models.Kweet;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.List;
import java.util.UUID;

@Stateless
public class KweetService {

    @EJB(beanName = "KweetDAOImpl")
    private IKweetDAO kweetDAO;

    public void postKweet(UUID userId, Kweet kweet){
        kweet.setAuthor(userId);
        kweetDAO.addKweet(kweet);
    }

    public void heartKweet(UUID userId, Kweet kweet){
        kweet = kweetDAO.getKweetById(kweet.getId());
        kweetDAO.heartKweet(userId, kweet);
    }

    public void removeKweet(Kweet kweet){
        kweet = kweetDAO.getKweetById(kweet.getId());
        kweetDAO.removeKweet(kweet);
    }

    public List<Kweet> getAllUserKweets(UUID userId){
        return kweetDAO.getAllUserKweets(userId);
    }

    public List<Kweet> getDashboard(UUID userId, List<UUID> following, int resultPage, int resultSize){
        return kweetDAO.getDashboard(userId, following, resultPage, resultSize);
    }

    public List<Kweet> getSearchResult(String searchQuery, int resultPage, int resultSize){return kweetDAO.getSearchResult(searchQuery, resultPage, resultSize);}

    public void setKweetDAO(IKweetDAO kweetDAO){
        this.kweetDAO = kweetDAO;
    }
}
