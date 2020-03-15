package org.aja.service;

import org.aja.api.User;
import org.aja.client.UserClient;

import java.util.List;
import java.util.concurrent.Callable;

public class UserService {

    private final UserClient userClient;

    public UserService(UserClient userClient) {
        this.userClient = userClient;
    }

    public List<User> getUsers() throws Exception {
        Thread.sleep(1000);
        return userClient.getUsers();
    }

    public User getUser(String user) {
        return userClient.getUser(user);
    }

    public Callable<List<User>> users() throws Exception {

        System.out.println("TH" + System.currentTimeMillis());

        return () -> getUsers();
    }
}
