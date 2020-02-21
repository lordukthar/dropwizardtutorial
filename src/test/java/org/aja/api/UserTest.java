package org.aja.api;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static final String SECRET_KEY = "qwerty";
    String jwtId = "SOMEID1234";
    String jwtIssuer = "JWT Demo";
    String jwtSubject = "Andrew";
    int jwtTimeToLive = 800000;

    @Test
    public void test() {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(jwtId)
                .setIssuedAt(now)
                .setSubject(jwtSubject)
                .setIssuer(jwtIssuer)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (jwtTimeToLive > 0) {
            long expMillis = nowMillis + jwtTimeToLive;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        System.out.println(builder.compact());

    }
}