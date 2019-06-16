package models;

import dtos.KweetDTO;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "KWEET")
@NamedQueries({
        @NamedQuery(name = "Kweet.getKweetById", query = "SELECT k FROM Kweet k WHERE k.id = :id"),
        @NamedQuery(name = "Kweet.getAllUserKweets", query = "SELECT k FROM Kweet k WHERE k.author = :user ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.getDashboard", query = "SELECT k FROM Kweet k WHERE :currentUser = k.author OR k.author IN :following ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.searchKweets", query = "SELECT k FROM Kweet k WHERE k.message LIKE :searchQuery ORDER BY k.createdAt DESC"),
})
public class Kweet {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private UUID id;

    @Column(name = "message")
    private String message;

    @ElementCollection
    @Column(name = "hearts")
    private List<UUID> hearts = new ArrayList<>();

    @Column(name = "author")
    private UUID author;

    @CreationTimestamp
    private Date createdAt;

    public Kweet(){
        //Empty constructor for JPA
    }

    public Kweet(KweetDTO kweetDTO){
        this.id = kweetDTO.getId();
        this.message = kweetDTO.getMessage();
    }

    public UUID getId() {
        return id;
    }

    public void  setId(UUID id){this.id = id;}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UUID> getHearts() {
        return hearts;
    }

    public void setHearts(List<UUID> hearts) {
        this.hearts = hearts;
    }

    public UUID getAuthor() {
        return author;
    }

    public void setAuthor(UUID author) {
        this.author = author;
    }
}
