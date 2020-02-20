package org.aja.resources;


import org.aja.api.User;
import org.aja.client.UserClient;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Path("/users")
public class UserResource {

    private final Executor executor = Executors.newFixedThreadPool(5);
    private final UserClient userClient;

    public UserResource(UserClient userClient) {
        this.userClient = userClient;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getUsers(@Suspended final AsyncResponse resp) {

        executor.execute(() -> {
            try {
                resp.resume(userClient.getUsers());
            } catch (Exception e) {
                resp.resume(e);
            }
        });
    }

    @GET()
    @Path("/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getUser(@Suspended final AsyncResponse resp, @PathParam("user") String user) {


            if (user.equals("ERROR")) {
                throw new WebApplicationException();
            }

            resp.resume(new User(user));


    }

}
