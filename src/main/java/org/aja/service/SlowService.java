package org.aja.service;

import org.aja.api.User;
import org.aja.client.UserClient;
import org.apache.log4j.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;


public class SlowService {

    private static final Logger log = Logger.getLogger(SlowService.class);

    private final UserClient userClient;

    public SlowService(UserClient userClient) {
        this.userClient = userClient;
    }

    public void slowMethod(User user)
            throws InterruptedException {

        System.out.println("SlowService started for "
                + "User ID: " + user.getId());

        Thread.sleep(5000);

        System.out.println("SlowService ended for "
                + "User ID: " + user.getId());
    }


    public Mono<User> slowMono(String userName)
            throws InterruptedException {

        System.out.println("SlowService started for "
                + "User ID: " +userName);

        Thread.sleep(1000);

        final User user = userClient.getUser(userName);

        System.out.println("SlowService ended for "
                + "User ID: " +userName);

        return Mono.just(user);
    }

    public Flux<User> slowFlux()
            throws InterruptedException {

        System.out.println("slowFlux started");

        Thread.sleep(1000);

        List<User> users = userClient.getUsers();

        User[] myArray = new User[users.size()];
        users.toArray(myArray);

        System.out.println("slowFlux stopped:"+myArray.length);

        return Flux.just(myArray);

    }
}
