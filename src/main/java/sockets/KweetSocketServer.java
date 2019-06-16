package sockets;

import com.google.gson.Gson;
import dtos.KweetDTO;
import dtos.UserDTO;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/kweets/{username}")
@Stateless
public class KweetSocketServer {

    private Gson gson = new Gson();

    private Session session;
    private static Set<KweetSocketServer> endpoints = new CopyOnWriteArraySet<>();
    private static HashMap<String, String> users = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        this.session = session;
        endpoints.add(this);
        users.put(session.getId(), username);
    }

    public void spreadKweet(KweetDTO kweet, String token){
        Client client = ClientBuilder.newClient();
        List<UserDTO> followers= client.target("http://localhost:8080/Kwetter/api/v1/user/getFollowers/" + kweet.getAuthor().getId())
                .request(MediaType.APPLICATION_JSON)
                .header("authorization", token)
                .get(new GenericType<List<UserDTO>>(){} );

        for (KweetSocketServer endpoint : endpoints){
            synchronized (kweet) {
                try {
                    if(followers.stream().filter(o -> o.getUsername().equals(users.get(endpoint.session.getId()))).findFirst().isPresent()) {
                        endpoint.session.getBasicRemote().sendText(gson.toJson(kweet, KweetDTO.class));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
