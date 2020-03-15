package org.aja.resources;


import org.aja.api.User;
import org.aja.client.UserClient;
import org.aja.service.UserService;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/users")
public class UserResource {

    private final Executor executor = Executors.newFixedThreadPool(200);
    private final ExecutorService executorService;
    private final UserService userService;
    Logger log = Logger.getLogger("UserResource");

    public UserResource(ExecutorService executorService, UserService userService) {
        this.executorService = executorService;
        this.userService = userService;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsers(@Suspended final AsyncResponse resp, @Context SecurityContext context) throws Exception {
        long startTime = System.currentTimeMillis();
        log.info(Thread.currentThread().getName());

        try {
            resp.resume(Response.status(200).entity(userService.getUsers()).build());
        } finally {
            long endTime = System.currentTimeMillis();
            long time = endTime-startTime;
            log.info("Millis roundtrip: "+ time);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void create(@Suspended final AsyncResponse resp, @Context SecurityContext context) throws Exception {

        log.info(Thread.currentThread().getName());

        resp.resume(Response.status(200).entity(userService.getUsers()).build());
    }

    @GET()
    @Path("/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUser(@Suspended final AsyncResponse resp, @PathParam("user") String user, @Context SecurityContext context) throws Exception {

        log.info(Thread.currentThread().getName());

        resp.resume(Response.status(200).entity(userService.getUser(user)).build());




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
    public void getUsersByThread(@Suspended final AsyncResponse resp, @Context SecurityContext context) throws Exception {

        log.info("getUsersByThread" + Thread.currentThread().getName());
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                log.info("T" + Thread.currentThread().getName());
                resp.resume(Response.status(200).entity(userService.getUsers()).build());
            } catch (Exception e) {
                resp.resume(e);
            }
        }).start();

    }

    @GET()
    @Path("/test/exec")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsersByExecutor(@Suspended final AsyncResponse resp) throws Exception {

        long startTime = System.currentTimeMillis();
        log.info(Thread.currentThread().getName());

        final Callable<List<User>> users1 = userService.users();
       final Callable<List<User>> users2 = userService.users();
        final Callable<List<User>> users3 = userService.users();

        final Future<List<User>> submit1 = executorService.submit(users1);
        final Future<List<User>> submit2 = executorService.submit(users2);
        final Future<List<User>> submit3 = executorService.submit(users3);

        long midTime = System.currentTimeMillis();
        long time = midTime-startTime;
        log.info("Millis midTime : " + time);

        executorService.invokeAll(Arrays.asList(users1, users2, users3));

       List<User> users = new ArrayList<>();

        try {

         //if (submit1.isDone() && submit2.isDone() && submit3.isDone()) {
                users.addAll(submit1.get());
                users.addAll(submit2.get());
                users.addAll(submit3.get());
                resp.resume(Response.status(200).entity(users).build());
            //}

            //resp.resume(Response.status(200).entity(users).build());

        } finally {
            long endTime = System.currentTimeMillis();
            time = endTime-startTime;
            log.info("Millis roundtrip: " + time);
        }


        /*executorService.execute(() -> {
            try {
                log.info("T" + Thread.currentThread().getName());
                //Slow way calling it sequential
                final List<User> users = userService.getUsers();
                users.addAll(userService.getUsers());
                users.addAll(userService.getUsers());
                users.addAll(userService.getUsers());
                users.addAll(userService.getUsers());
                users.addAll(userService.getUsers());
                resp.resume(Response.status(200).entity(users).build());
            } catch (Exception e) {
                resp.resume(e);
            } finally {
                long endTime = System.currentTimeMillis();
                long time = endTime-startTime;
                log.info("Millis roundtrip: " + time);
            }
        });*/

    }

}
