package org.aja.service;

import org.aja.api.User;
import org.aja.client.UserClient;

import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ForkJoinTask extends RecursiveTask<List<User>> {

    private final UserService userService;

    public ForkJoinTask(UserService userService) {
        this.userService = userService;
    }

    @Override
    public List<User> compute() {
        return userService.getUsers();
    }

}
