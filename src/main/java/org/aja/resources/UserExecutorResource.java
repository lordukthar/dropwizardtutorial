package org.aja.resources;


import org.aja.service.UserService;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ManagedAsync;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.concurrent.ExecutorService;

@Path("/usersexecutorservice")
public class UserExecutorResource {

    private final ExecutorService executorService;
    private final UserService userService;
    private Logger log = Logger.getLogger("UserResource");

    public UserExecutorResource(ExecutorService executorService, UserService userService) {
        this.executorService = executorService;
        this.userService = userService;
    }

    @GET
    @Path("/injected")
    @Produces(MediaType.APPLICATION_JSON)
    @ManagedAsync
    public void getUsersInjected(@Suspended final AsyncResponse resp, @Context SecurityContext context) {
        long startTime = System.currentTimeMillis();
        log.info("getUsers:" + Thread.currentThread().getName());

        try {
            resp.resume(Response.status(200).entity(userService.getUsers()).build());
        } finally {
            long endTime = System.currentTimeMillis();
            long time = endTime-startTime;
            log.info("Millis roundtrip: "+ time);
        }
    }

    @GET
    @Path("/manual")
    @Produces(MediaType.APPLICATION_JSON)
    public void getUsersManual(@Suspended final AsyncResponse resp, @Context SecurityContext context) {
        long startTime = System.currentTimeMillis();
        log.info("getUsersManual_1:" + Thread.currentThread().getName());

        try {
            executorService.execute(() -> {
                log.info("getUsersManual_2:" + Thread.currentThread().getName());
                resp.resume(userService.getUsers());
            });

        } finally {
            long endTime = System.currentTimeMillis();
            long time = endTime-startTime;
            log.info("Millis roundtrip: "+ time);
        }
    }


}
