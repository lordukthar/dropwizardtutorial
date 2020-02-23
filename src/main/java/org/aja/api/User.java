package org.aja.api;

import java.util.Arrays;
import java.util.List;

public class User {

    private final String name;
    private final List<Self> links;

    public User(String name) {
        this.name = name;
        this.links = Arrays.asList(new Self("http://localhost:8960/blog-service/users/"+name));
    }

    public String getName() {
        return name;
    }

    public List<Self> getLinks() {
        return links;
    }
}
