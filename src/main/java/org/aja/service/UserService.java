package org.aja.service;

import org.aja.api.User;
import org.aja.client.RxUserClient;
import org.aja.client.UserClient;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import javax.ws.rs.WebApplicationException;
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
                .doOnSubscribe(()
                        -> System.out.println("defer getObservableUsers------------------------->" + Thread.currentThread().getName()))
                .onErrorReturn(emptyUserList())
                .subscribeOn(Schedulers.newThread())
                .doOnTerminate(() -> System.out.println("INFO -----> terminating" )));
    }

    private Func1<Throwable, List<User>> emptyUserList() {
        return throwable -> {
            System.out.println("ERR---------->" + throwable);
            throw new WebApplicationException(throwable);
        };
    }

    private Func1<Throwable, User> emptyUser() {
        return throwable -> {
            System.out.println("ERR---------->" + throwable);
            throw new WebApplicationException(throwable);
        };
    }


    //now lets fetch all users then match it to one user and return that user's data.
    public Observable<User> getObservableUser(String userName) throws Exception {

        System.out.println("-----------------------------------");



        var user =  Observable.defer(() -> rxUserClient.getUsersAsync()
                .flatMapIterable(x -> x)
                .filter(u -> u.getName().equalsIgnoreCase(userName))
                .first()
                .timeout(5, TimeUnit.SECONDS)
                .doOnSubscribe(()
                        -> System.out.println("defer getObservableUsers------------------------->" + Thread.currentThread().getName()))
                .subscribeOn(Schedulers.newThread())
                .onErrorReturn(emptyUser())
                .doOnTerminate(() -> System.out.println("INFO -----> terminating" )));

        //return user;


        /*

        var user = Observable.defer(() -> rxUserClient.getUsersAsync()
                .flatMap(Observable::from)
                .filter(u -> u.getName().equalsIgnoreCase(userName))
                .timeout(5, TimeUnit.SECONDS)
                .doOnSubscribe(()
                        -> System.out.println("defer getObservableUser------------------------->" + Thread.currentThread().getName()))
                .onErrorReturn(emptyUser())
                .doOnTerminate(() -> System.out.println("INFO -----> terminating" )));

        System.out.println("-----------------------------------");

        return user;*/

        return user.flatMap(u -> Observable.defer(() ->
                rxUserClient.getUserAsync(u.getId())
                        .doOnSubscribe(()
                                -> System.out.println("defer getObservableUser 2------------------------->" + Thread.currentThread().getName()))

                        .subscribeOn(Schedulers.io())));
    }

}
