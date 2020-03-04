package org.aja.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.aja.api.User;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserClient {

    private final static String URL = "http://jsonplaceholder.typicode.com/users";


    public User getUser(String name) {
        System.out.println("UserClient: " + Thread.currentThread().getName());
        return new User(name);
    }


    public List<User> getUsers() {

        System.out.println("Client getUsers" + Thread.currentThread().getName());

        try {

            HttpHost proxy = new HttpHost("trend3.sbab.ad", 8080, "http");


            RequestConfig.Builder reqconfigconbuilder= RequestConfig.custom();
            reqconfigconbuilder = reqconfigconbuilder.setProxy(proxy);
            RequestConfig config = reqconfigconbuilder.build();

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URL);
            httpGet.setConfig(config);

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
                Charset charset = contentType.getCharset();
                Reader reader = new InputStreamReader(entity.getContent(), charset);

                Type collectionType = new TypeToken<Collection<User>>(){}.getType();
                return gson.fromJson(reader, collectionType);
            };

            return httpclient.execute(httpGet, rh);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ArrayList<>();

    }
}
