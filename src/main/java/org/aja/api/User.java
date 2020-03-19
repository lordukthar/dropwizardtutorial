package org.aja.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty("name")
    private final String name;

    @JsonProperty("links")
    private List<Self> links;

    @JsonProperty
    private int id;

    private List<Post> posts;

    @ConstructorProperties({"name", "id"})
    public User(String name, int id) {
        this.name = name;
        this.id = id;
        posts = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public List<Self> getLinks() {

        return Arrays.asList(new Self("http://localhost:8960/blog-service/users/"+name));
    }
}
