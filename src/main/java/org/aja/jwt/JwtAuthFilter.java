package org.aja.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.annotation.Priority;
import javax.crypto.KeyGenerator;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.security.Principal;
import java.util.regex.Pattern;

@Priority(Priorities.AUTHORIZATION)
public final class JwtAuthFilter<P extends Principal>
        implements ContainerRequestFilter {

        private static final Pattern AUTHORIZATION_HEADER_PATTERN = Pattern.compile("Bearer ([^.]+\\.[^.]+\\.[^.]+)");
        private static final String AUTHENTICATION_SCHEME_NAME = "JWT";

        private static final String SECRET_KEY = "qwerty";


    private KeyGenerator keyGenerator;


        public JwtAuthFilter()
                throws NullPointerException {



        }


    @Override
    public void filter(ContainerRequestContext requestContext) throws WebApplicationException, IOException {
        // Get the HTTP Authorization header from the request
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);


        try {

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                throw new NotAuthorizedException("Authorization header must be provided");
            }
            String token = authorizationHeader.substring("Bearer".length()).trim();


            // Validate the token
            Claims c = decodeJWT(token);
            System.out.println(c.getIssuer());
            return;

        } catch (Exception e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }



    }

    public static Claims decodeJWT(String jwt) {
        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }


}
