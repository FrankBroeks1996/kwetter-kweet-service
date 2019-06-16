package daos;

import dtos.UserDTO;
import models.Kweet;

import javax.ejb.Stateless;
import javax.enterprise.inject.Default;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;

@Stateless
@Default
@Named("KweetDAOImpl")
public class KweetDAOImpl implements IKweetDAO {

    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    public void addKweet(Kweet kweet){
        em.persist(kweet);
    }

    public void editKweet(Kweet kweet){
        em.merge(kweet);
    }

    public void removeKweet(Kweet kweet){
        kweet = getKweetById(kweet.getId());
        em.remove(kweet);
    }

    public Kweet getKweetById(UUID id){
        try {
            return em.createNamedQuery("Kweet.getKweetById", Kweet.class).setParameter("id", id).getSingleResult();
        }
        catch (Exception e){
            return null;
        }
    }

    public void heartKweet(UUID user, Kweet kweet){
        kweet.getHearts().add(user);
    }

    public List<Kweet> getAllUserKweets(UUID user){
        return em.createNamedQuery("Kweet.getAllUserKweets", Kweet.class)
                .setParameter("user", user)
                .getResultList();
    }

    public List<Kweet> getDashboard(UUID user, List<UUID> following, int resultPage, int resultSize){
        return em.createNamedQuery("Kweet.getDashboard", Kweet.class)
                .setFirstResult((resultPage-1) * resultSize)
                .setMaxResults(resultSize)
                .setParameter("currentUser", user)
                .setParameter("following", following)
                .getResultList();
    }

    public List<Kweet> getSearchResult(String searchQuery, int resultPage, int resultSize){
        return em.createNamedQuery("Kweet.searchKweets", Kweet.class)
                .setFirstResult((resultPage-1) * resultSize)
                .setMaxResults(resultSize)
                .setParameter("searchQuery", "%" + searchQuery + "%").getResultList();
    }
}
