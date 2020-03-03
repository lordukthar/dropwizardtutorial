package org.aja.resources;


import org.aja.api.User;
import org.aja.client.UserClient;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Path("/users")
public class UserResource {

    private final Executor executor = Executors.newFixedThreadPool(200);
    private final UserClient userClient;
    Logger log = Logger.getLogger("UserResource");

    public UserResource(UserClient userClient) {
        this.userClient = userClient;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getUsers(@Suspended final AsyncResponse resp) {

        System.out.println(Thread.currentThread().getName());

        resp.resume(Response.status(200).entity(Arrays.asList(userClient.getUser("aaa"))).build());
    }

    @GET()
    @Path("/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getUser(@Suspended final AsyncResponse resp, @PathParam("user") String user) throws WebApplicationException {


       // try {

        System.out.println(Thread.currentThread().getName());


        executor.execute(() -> {
            try {
                resp.resume(Response.status(200).entity(userClient.getUser(user)).build());
            } catch (Exception e) {
                resp.resume(e);
            }
        });





    }

}
