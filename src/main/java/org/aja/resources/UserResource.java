package org.aja.resources;


import org.aja.api.User;
import org.aja.service.ForkJoinTask;
import org.aja.service.UserService;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Path("/users")
public class UserResource {

    private final Executor executor = Executors.newFixedThreadPool(200);
    private final ExecutorService executorService;
    private final UserService userService;
    Logger log = Logger.getLogger("UserResource");
    ForkJoinPool pool = new ForkJoinPool(10);

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
    public void getUsersByThread(@Suspended final AsyncResponse resp) throws Exception {

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

    //The OLD executorservice way
    @GET()
    @Path("/test/exec")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsersByExecutor(@Suspended final AsyncResponse resp, @Context SecurityContext context) throws Exception {

        long startTime = System.currentTimeMillis();
        log.info(Thread.currentThread().getName());

        Callable<List<User>> users1 = userService.users();
        Callable<List<User>> users2 = userService.users();
        Callable<List<User>> users3 = userService.users();

        final List<Future<List<User>>> futures = executorService.invokeAll(Arrays.asList(users1, users2, users3));

        List<User> users = new ArrayList<>();

        try {
            for (Future<List<User> >fus : futures) {
                users.addAll(fus.get());
            }

            resp.resume(Response.status(200).entity(users).build());

        } finally {
            long endTime = System.currentTimeMillis();
            long time = endTime-startTime;
            log.info("Millis roundtrip: " + time);
        }
    }

    //The fork join
    @GET()
    @Path("/test/fork")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsersByForJoin(@Suspended final AsyncResponse resp, @Context SecurityContext context){


        long startTime = System.currentTimeMillis();
        log.info(Thread.currentThread().getName());

        final ForkJoinTask forkJoinTask = new ForkJoinTask(userService);
        final ForkJoinTask forkJoinTask1 = new ForkJoinTask(userService);

        pool.execute(forkJoinTask);
        pool.execute(forkJoinTask1);

        List<User> users = forkJoinTask.join();
        users.addAll(forkJoinTask1.join());

        try {
            resp.resume(Response.status(200).entity(users).build());

        } finally {
            long endTime = System.currentTimeMillis();
            long time = endTime-startTime;
            log.info("Millis roundtrip: " + time);
        }
    }

    //rxJava way
    @GET()
    @Path("/test/rxjava")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsersByRxJava(@Suspended final AsyncResponse resp, @Context SecurityContext context) throws Exception {

        log.info("managed------------------------->" + Thread.currentThread().getName());

        long startTime = System.currentTimeMillis();
        try {

        userService.getObservableUsers()
                .timeout(30, TimeUnit.SECONDS)
                .subscribe((users) -> {
                    log.info("getObservableUsers------------------------->" + Thread.currentThread().getName());
                    resp.resume(Response.status(200).entity(users).build());
                },
                 throwable -> {
                    log.info("getObservableUsers t------------------------->" + Thread.currentThread().getName());
                    resp.resume(Response.status(500).build());
                });




        } finally {
            long endTime = System.currentTimeMillis();
            long time = endTime-startTime;
            log.info("Millis roundtrip: " + time);
        }
    }
}
