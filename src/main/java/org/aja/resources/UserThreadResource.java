package org.aja.resources;


import org.aja.service.UserService;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/usersthread")
public class UserThreadResource {

    private final UserService userService;
    private Logger log = Logger.getLogger("UserResource");

    public UserThreadResource(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/")
    public void getUsersByThreading(@Suspended final AsyncResponse resp, @Context SecurityContext context) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                log.info("getUsersByThreading: " + Thread.currentThread().getName());
                resp.resume(Response.status(200).entity(userService.getUsers()).build());
            } catch (InterruptedException e) {
                resp.resume(e);
            }
        }).start();
    }

}
