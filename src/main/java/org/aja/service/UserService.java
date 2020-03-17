package org.aja.service;

import org.aja.api.User;
import org.aja.client.RxUserClient;
import org.aja.client.UserClient;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class UserService {

    private final UserClient userClient;
    private final RxUserClient rxUserClient;

    public UserService(UserClient userClient, RxUserClient rxUserClient) {
        this.userClient = userClient;
        this.rxUserClient = rxUserClient;
    }

    public List<User> getUsers() {
        System.out.println("GET USERS -------------------------------------------->");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return userClient.getUsers();
    }

    public User getUser(String user) {
        return userClient.getUser(user);
    }

    public Callable<List<User>> users() throws Exception {
        return () -> getUsers();
    }

    public Observable<List<User>> getObservableUsers() throws Exception {

        return Observable.defer(() -> rxUserClient.getUsersAsync()
                .timeout(5, TimeUnit.SECONDS)
                .doOnNext((x) -> {
                    System.out.println("getObservableUsers------------------------->" + Thread.currentThread().getName());
                })
                .doOnError(throwable -> {
                    System.out.println("ERR ----->" + throwable);
                })
                .subscribeOn(Schedulers.newThread())
                .doOnTerminate(() -> {
                    System.out.println("INFO -----> terminating" );
                }));
    }
}
