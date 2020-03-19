package org.aja.client;

import org.aja.api.Post;
import org.aja.api.User;
import org.apache.http.HttpHost;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import rx.Observable;

import javax.ws.rs.core.GenericType;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class RxUserClient {



    private static final GenericType<List<User>> USERS_LIST = new GenericType<>() {
    };

    private static final GenericType<List<Post>> POSTS_LIST = new GenericType<>() {
    };

    HttpHost proxy = new HttpHost("trend3.sbab.ad", 8080, "http");

    private final ExecutorService threadPoolExecutor = new ThreadPoolExecutor(10, 200,
            60L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>());

    private final RxClient<RxObservableInvoker> client;

    private final static String USERS_PATH = "/users";
    private final static String POSTS_PATH = "/posts";
    private final  URI baseUri;

    public RxUserClient(RxClient<RxObservableInvoker> client, URI baseUri) {
        this.baseUri = baseUri;
        this.client = client;
      }

    public Observable<List<User>> getUsersAsync() {
        return  client.target(baseUri)
                .path(USERS_PATH)
                .request()
                .rx(threadPoolExecutor)
                .get(USERS_LIST);
    }

    //fetch posts
    public Observable<List<Post>> getPostsAsync() {
        return  client.target(baseUri)
                .path(POSTS_PATH)
                .request()
                .rx(threadPoolExecutor)
                .get(POSTS_LIST);
    }

    public Observable<User> getUserAsync(int id) {
        return  client.target(baseUri)
                .path(USERS_PATH+"/"+id)
                .request()
                .rx(threadPoolExecutor)
                .get(User.class);
    }

}
