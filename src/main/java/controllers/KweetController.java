package controllers;

import com.google.gson.Gson;
import dtos.KweetDTO;
import dtos.UserDTO;
import models.Kweet;
import security.TokenNeeded;
import services.KweetService;
import sockets.KweetSocketServer;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.security.Principal;
import java.util.*;

@Path("kweet")
public class KweetController {
    @EJB
    private KweetService kweetService;

    @EJB
    private KweetSocketServer kweetSocketServer;

    @Context
    private SecurityContext context;

    @Context
    private HttpHeaders headers;

    private Gson gson = new Gson();

    @POST
    @Path("post")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @TokenNeeded
    public Response postKweet(KweetDTO kweetDTO){
        try {
            Principal principal = context.getUserPrincipal();
            String userId = principal.getName();
            Kweet kweet = new Kweet(kweetDTO);

            kweetService.postKweet(UUID.fromString(userId), kweet);

            Client client = ClientBuilder.newClient();
            UserDTO author = client.target("http://localhost:8080/Kwetter/api/v1/user/getUserById/" + userId)
                    .request(MediaType.APPLICATION_JSON)
                    .header("authorization", headers.getHeaderString("authorization"))
                    .get(new GenericType<UserDTO>(){} );

            KweetDTO returnDto = new KweetDTO(kweet);
            returnDto.setAuthor(author);

            kweetSocketServer.spreadKweet(returnDto, headers.getHeaderString("authorization"));

            return Response.ok().entity(returnDto).build();
        }
        catch (Exception e){
            e.printStackTrace();
            return Response.status(500).build();
        }
    }

    @POST
    @Path("heart")
    @Consumes(MediaType.APPLICATION_JSON)
    @TokenNeeded
    public void heartKweet(KweetDTO kweetDTO){
        Principal principal = context.getUserPrincipal();
        String userId = principal.getName();
        Kweet kweet = new Kweet(kweetDTO);

        kweetService.heartKweet(UUID.fromString(userId), kweet);
    }

    @POST
    @Path("removeKweet")
    @Consumes(MediaType.APPLICATION_JSON)
    @TokenNeeded
    public void removeKweet(KweetDTO kweetDTO){
        Kweet kweet = new Kweet(kweetDTO);
        kweetService.removeKweet(kweet);
    }

    @GET
    @Path("getAllKweets/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KweetDTO> getAllKweets(@PathParam("userId")String userId){
        List<KweetDTO> userKweetDtoList = new ArrayList<>();

        Client client = ClientBuilder.newClient();
        UserDTO author = client.target("http://localhost:8080/Kwetter/api/v1/user/getUserById/" + userId)
                .request(MediaType.APPLICATION_JSON)
                .header("authorization", headers.getHeaderString("authorization"))
                .get(new GenericType<UserDTO>(){} );

        for (Kweet kweet : kweetService.getAllUserKweets(UUID.fromString(userId))){
            KweetDTO kweetDTO = new KweetDTO(kweet);
            kweetDTO.setAuthor(author);
            userKweetDtoList.add(kweetDTO);
        }

        return userKweetDtoList;
    }

    @GET
    @Path("getDashboard")
    @Produces(MediaType.APPLICATION_JSON)
    @TokenNeeded
    public List<KweetDTO> getDashboard(@QueryParam("resultPage") int resultPage, @QueryParam("resultSize") int resultSize){
        Principal principal = context.getUserPrincipal();
        String userId = principal.getName();

        Client client = ClientBuilder.newClient();
        List<UserDTO> following = client.target("http://localhost:8080/Kwetter/api/v1/user/getFollowing/" + userId)
                .request(MediaType.APPLICATION_JSON)
                .header("authorization", headers.getHeaderString("authorization"))
                .get(new GenericType<List<UserDTO>>(){} );

        UserDTO currentUser = client.target("http://localhost:8080/Kwetter/api/v1/user/getUserById/" + userId)
                .request(MediaType.APPLICATION_JSON)
                .header("authorization", headers.getHeaderString("authorization"))
                .get(new GenericType<UserDTO>(){} );

        following.add(currentUser);
        List<KweetDTO> userDashboard = new ArrayList<>();

        List<UUID> uuids = new ArrayList<>();
        for (UserDTO user: following) {
            uuids.add(user.getId());
        }


        for (Kweet kweet : kweetService.getDashboard(UUID.fromString(userId), uuids, resultPage, resultSize)){
            KweetDTO completeKweet = new KweetDTO(kweet);
            completeKweet.setAuthor(following.stream().filter(userDTO -> userDTO.getId().equals(kweet.getAuthor())).findFirst().get());
            userDashboard.add(completeKweet);
        }

        return userDashboard;
    }

    @GET
    @Path("search/{searchQuery}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KweetDTO> search(@PathParam("searchQuery") String searchQuery, @QueryParam("resultPage") int resultPage, @QueryParam("resultSize") int resultSize){
        List<KweetDTO> searchResult = new ArrayList<>();

        List<Kweet> kweets = kweetService.getSearchResult(searchQuery, resultPage, resultSize);

        Set<UUID> authorIds = new HashSet<>();
        for (Kweet kweet : kweets){
            authorIds.add(kweet.getAuthor());
        }

        Client client = ClientBuilder.newClient();
        List<UserDTO> authors = client.target("http://localhost:8080/Kwetter/api/v1/user/getUsers/")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(authorIds, MediaType.APPLICATION_JSON)).readEntity(new GenericType<List<UserDTO>>(){});

        for (Kweet kweet : kweets){
            KweetDTO completeKweet = new KweetDTO(kweet);
            completeKweet.setAuthor(authors.stream().filter(userDTO -> userDTO.getId().equals(kweet.getAuthor())).findFirst().get());
            searchResult.add(completeKweet);
        }

        return searchResult;
    }
}
