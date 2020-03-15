package org.aja.resources;


import org.aja.client.UserClient;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("/users")
public class UserResource {

    private final Executor executor = Executors.newFixedThreadPool(200);
    private final ExecutorService executorService;
    private final UserClient userClient;
    Logger log = Logger.getLogger("UserResource");

    public UserResource(ExecutorService executorService, UserClient userClient) {
        this.executorService = executorService;
        this.userClient = userClient;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsers(@Suspended final AsyncResponse resp, @Context SecurityContext context) {

        System.out.println(Thread.currentThread().getName());

        resp.resume(Response.status(200).entity(userClient.getUsers()).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void create(@Suspended final AsyncResponse resp, @Context SecurityContext context) {

        System.out.println(Thread.currentThread().getName());

        resp.resume(Response.status(200).entity(userClient.getUsers()).build());
    }

    @GET()
    @Path("/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUser(@Suspended final AsyncResponse resp, @PathParam("user") String user, @Context SecurityContext context) throws WebApplicationException {

        System.out.println(Thread.currentThread().getName());

        resp.resume(Response.status(200).entity(userClient.getUser(user)).build());




        /*executor.execute(() -> {
            try {
                resp.resume(Response.status(200).entity(userClient.getUser(user)).build());
            } catch (Exception e) {
                resp.resume(e);
            }
        });*/





    }

    @GET()
    @Path("/test/thread")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsersByThread(@Suspended final AsyncResponse resp, @Context SecurityContext context) throws WebApplicationException {

        System.out.println("getUsersByThread" + Thread.currentThread().getName());
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("T" + Thread.currentThread().getName());
                resp.resume(Response.status(200).entity(userClient.getUsers()).build());
            } catch (InterruptedException e) {
                resp.resume(e);
            }
        }).start();

    }

    @GET()
    @Path("/test/exec")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsersByExecutor(@Suspended final AsyncResponse resp, @Context SecurityContext context) throws WebApplicationException {

        System.out.println(Thread.currentThread().getName());
        executorService.execute(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("T" + Thread.currentThread().getName());
                resp.resume(Response.status(200).entity(userClient.getUsers()).build());
            } catch (Exception e) {
                resp.resume(e);
            }
        });

    }

}
