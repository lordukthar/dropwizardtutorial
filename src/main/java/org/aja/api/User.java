package org.aja.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;
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

    @ConstructorProperties({"name", "id"})
    public User(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Self> getLinks() {

        return Arrays.asList(new Self("http://localhost:8960/blog-service/users/"+name));
    }
}
