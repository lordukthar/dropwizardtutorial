package org.aja.resources;


import org.aja.api.User;
import org.aja.service.SlowService;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Path("/usersreact")
public class UserRectorResource {

    private final SlowService slowService;
    private Logger log = Logger.getLogger("UserResource");

    public UserRectorResource(SlowService slowService) {
        this.slowService = slowService;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsers(@Suspended final AsyncResponse resp, @Context SecurityContext context) throws Exception {
        long startTime = System.currentTimeMillis();
        log.info("getUsers:" + Thread.currentThread().getName());

        try {

            List<User> users = new ArrayList<>();

            final Flux<User> userFlux = slowService.slowFlux();


            userFlux.flatMap(a -> Mono.just(a).subscribeOn(Schedulers.parallel()))
                    .doOnNext(
                            a ->
                            {   System.out.println(
                                            "Received: " + a + " on thread: " + Thread.currentThread().getName());
                                users.add(a);
                                System.out.println(
                                        "Adding " + a + " on thread: " + Thread.currentThread().getName());

                            })
                    .flatMap(
                            a -> {
                                System.out.println(
                                        "Received in flatMap: " + a + " on thread: " + Thread.currentThread().getName());

                                return Mono.just(a).subscribeOn(Schedulers.elastic());
                            })
                    .subscribe(
                            a ->
                                    System.out.println(
                                            "Received (in the subscriber): "
                                                    + a
                                                    + " on thread: "
                                                    + Thread.currentThread().getName()));




            resp.resume(Response.status(200).entity(users).build());
        } finally {
            long endTime = System.currentTimeMillis();
            long time = endTime-startTime;
            log.info("Millis roundtrip: "+ time);
        }
    }

    @GET()
    @Path("/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUser(@Suspended final AsyncResponse resp, @PathParam("user") String userName, @Context SecurityContext context)  throws InterruptedException {
        log.info("getUser:" + Thread.currentThread().getName());

       slowService.slowMono(userName)
                .doOnSubscribe((x) -> log.info(Thread.currentThread().getName()))
                .subscribeOn(Schedulers.elastic())
                .subscribe((user) -> {
                    resp.resume(Response.status(200).entity(user).build());
                });

    }

}
