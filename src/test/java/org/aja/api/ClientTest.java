package org.aja.api;

import com.codahale.metrics.MetricRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import io.dropwizard.setup.Environment;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ClientTest {


    @Test
    public void test() {

       String URL = "http://localhost:8960/blog-service/users/ERR";



            try {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet(URL);

                ResponseHandler<User> rh = response -> {
                    StatusLine statusLine = response.getStatusLine();
                    HttpEntity entity = response.getEntity();
                    if (statusLine.getStatusCode() >= 300) {
                        throw new HttpResponseException(
                                statusLine.getStatusCode(),
                                statusLine.getReasonPhrase());
                    }
                    if (entity == null) {
                        throw new ClientProtocolException("No content");
                    }
                    Gson gson = new GsonBuilder().create();
                    ContentType contentType = ContentType.getOrDefault(entity);
                    //Charset charset = contentType.getCharset();
                    Reader reader = new InputStreamReader(entity.getContent(), Charset.defaultCharset());

                    Type collectionType = new TypeToken<User>(){}.getType();
                    return gson.fromJson(reader, collectionType);
                };

                for (int i = 0; i < 50; i++) {
                User u = httpclient.execute(httpGet, rh);
                //System.out.println(u.getName());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }



        }



    @Test
    public void testUsers() {

        String URL = "http://localhost:8960/blog-service/users";



        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL);

            ResponseHandler<List<User>> rh = response -> {
                StatusLine statusLine = response.getStatusLine();
                HttpEntity entity = response.getEntity();
                if (statusLine.getStatusCode() >= 300) {
                    throw new HttpResponseException(
                            statusLine.getStatusCode(),
                            statusLine.getReasonPhrase());
                }
                if (entity == null) {
                    throw new ClientProtocolException("No content");
                }
                Gson gson = new GsonBuilder().create();
                ContentType contentType = ContentType.getOrDefault(entity);
                //Charset charset = contentType.getCharset();
                Reader reader = new InputStreamReader(entity.getContent(), Charset.defaultCharset());

                Type collectionType = new TypeToken<Collection<User>>(){}.getType();
                return gson.fromJson(reader, collectionType);
            };

            for (int i = 0; i < 50; i++) {
                httpclient.execute(httpGet, rh);
                //System.out.println(u.getName());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    @Test
    public void testAsynch() throws Exception {
        final WebTarget target = ClientBuilder.newClient()
                .target("http://localhost:8960/blog-service/users");


        for (int i = 0; i < 50; i++) {
            Future<User> user = target
                    .path("/{user}")
                    .resolveTemplate("user", "Jonas")
                    .request()
                    .async()
                    .get(User.class);
        }


    }

}





