package org.aja.mapper;

import org.aja.api.User;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {


    public Response toResponse(WebApplicationException exception) {
        return Response.status(404)
                .entity(new User("404"))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}