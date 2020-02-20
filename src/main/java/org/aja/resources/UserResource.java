package org.aja.resources;


import org.aja.client.UserClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
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
}
